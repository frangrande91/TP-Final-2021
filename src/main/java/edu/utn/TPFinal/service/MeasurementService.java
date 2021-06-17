package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exception.AccessNotAllowedException;
import edu.utn.TPFinal.exception.RestrictDeleteException;
import edu.utn.TPFinal.exception.notFound.*;
import edu.utn.TPFinal.model.*;
import edu.utn.TPFinal.model.dto.ReceivedMeasurementDto;
import edu.utn.TPFinal.model.dto.UserDto;
import edu.utn.TPFinal.model.response.ClientConsumption;
import edu.utn.TPFinal.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class MeasurementService {

    private static final String MEASUREMENT_PATH = "measurement";
    private MeasurementRepository measurementRepository;
    private MeterService meterService;
    private AddressService addressService;
    private UserService userService;


    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, MeterService meterService, AddressService addressService, UserService userService) {
        this.measurementRepository = measurementRepository;
        this.meterService = meterService;
        this.addressService = addressService;
        this.userService = userService;
    }

    public Page<Measurement> getAllByMeterAndDateBetween(Integer idMeter,Integer idUser, LocalDate from, LocalDate to, Pageable pageable) throws MeterNotExistsException, UserNotExistsException, AccessNotAllowedException {
        Meter meter = meterService.getMeterById(idMeter);
        User user = userService.getUserById(idUser);

        if(userService.containsMeter(user,meter) || user.getTypeUser().equals(TypeUser.EMPLOYEE)) {
            return measurementRepository.findAllByMeterAndDateBetween(meter,from,to,pageable);
        }
        else {
            throw new AccessNotAllowedException("You have not access to this resource");
        }
    }

    public Page<Measurement> getAllByAddressAndDateBetween(Integer idAddress, LocalDate from, LocalDate to, Pageable pageable) throws AddressNotExistsException {
        Address address = addressService.getAddressById(idAddress);
        return measurementRepository.findAllByMeterAndDateBetween(address.getMeter(), from, to, pageable);
    }

    public ClientConsumption getConsumptionByMeterAndDateBetween(Integer idMeter, Integer idQueryUser, LocalDate from, LocalDate to) throws MeterNotExistsException, UserNotExistsException, AccessNotAllowedException {
        Meter meter = meterService.getMeterById(idMeter);
        User queryUser = userService.getUserById(idQueryUser);
        User clientUser = meter.getAddress().getUserClient();

        double totalConsumptionKw = 0.0;
        double totalConsumptionMoney = 0.0;
        double lastMeasurementKw = 0.0;

        Pageable pageable = PageRequest.of(0,1, Sort.by(List.of(new Sort.Order(Sort.Direction.DESC, "date"))));

        if(userService.containsMeter(queryUser,meter) || queryUser.getTypeUser().equals(TypeUser.EMPLOYEE)) {

            List<Measurement> measurements = measurementRepository.findAllByMeterAndDateBetween(meter,from,to);

            if(!measurements.isEmpty()) {
                List<Measurement> lastMeasurementList = measurementRepository.findByMeterAndDateBefore(meter,from,pageable).toList();


                if (!lastMeasurementList.isEmpty()) {
                    lastMeasurementKw = lastMeasurementList.get(0).getQuantityKw();
                }

                for(Measurement m : measurements) {
                    totalConsumptionMoney += m.getPriceMeasurement();
                }

                totalConsumptionKw = measurements.get(measurements.size() - 1).getQuantityKw() - lastMeasurementKw;
                return ClientConsumption.builder()
                        .consumptionKw(totalConsumptionKw)
                        .consumptionMoney(totalConsumptionMoney)
                        .quantityMeasurements(measurements.size())
                        .from(from)
                        .to(to)
                        .clientUser(new UserDto(clientUser.getId(),clientUser.getFirstName(),clientUser.getLastName(),clientUser.getUsername(),clientUser.getTypeUser()))
                        .build();
            }
            else {
                return ClientConsumption.builder()
                        .consumptionKw(0.0)
                        .consumptionMoney(0.0)
                        .quantityMeasurements(0)
                        .from(from)
                        .to(to)
                        .clientUser(new UserDto(clientUser.getId(),clientUser.getFirstName(),clientUser.getLastName(),clientUser.getUsername(),clientUser.getTypeUser()))
                        .build();
            }
        }
        else {
            throw new AccessNotAllowedException("You have not access to this resource");
        }
    }

    public Measurement addMeasurement(ReceivedMeasurementDto receivedMeasurementDto) throws MeterNotExistsException {

        double quantityKw = receivedMeasurementDto.getValue();

        Meter meter = meterService.getMeterBySerialNumberAndPassword(receivedMeasurementDto.getSerialNumber(), receivedMeasurementDto.getPassword());
        Measurement measurement = Measurement.builder()
                                    .meter(meter)
                                    .quantityKw(quantityKw)
                                    .date(LocalDate.parse(receivedMeasurementDto.getDate().substring(0,10)))
                                    .build();

        return measurementRepository.save(measurement);
    }

    public Page<Measurement> getAllMeasurements(Pageable pageable) {
        return measurementRepository.findAll(pageable);
    }

    public Page<Measurement> getAllSpec(Specification<Measurement> measurementSpecification, Pageable pageable) {
        return measurementRepository.findAll(measurementSpecification,pageable);
    }

    public Page<Measurement> getAllSort(Integer page, Integer size, List<Sort.Order> orders) {
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        return measurementRepository.findAll(pageable);
    }

    public Measurement getMeasurementById(Integer id) throws MeasurementNotExistsException{
        return measurementRepository.findById(id).orElseThrow(() -> new MeasurementNotExistsException("Measurement not exists"));
    }

    public Measurement addMeterToMeasurement(Integer id, Integer idMeter) throws MeasurementNotExistsException, MeterNotExistsException {
        Meter meter = meterService.getMeterById(idMeter);
        Measurement measurement = getMeasurementById(id);
        measurement.setMeter(meter);
        return measurementRepository.save(measurement);
    }

    public void deleteMeasurementById(Integer id) throws MeasurementNotExistsException, RestrictDeleteException {
        Measurement measurement = getMeasurementById(id);
        if(isNull(measurement.getBill())) {
            measurementRepository.deleteById(id);
        }
        else {
            throw new RestrictDeleteException("this measurement could not be deleted because it has already been invoiced");
        }

    }


}
