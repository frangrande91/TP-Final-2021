package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.AccessNotAllowedException;
import edu.utn.TPFinal.exceptions.notFound.*;
import edu.utn.TPFinal.model.*;
import edu.utn.TPFinal.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    /*public Page<Measurement> getAllByUserAndAddress(Integer idUserClient,Integer idAddress, Pageable pageable) throws AddressNotExistsException, UserNotExistsException {
        User userClient = userService.getUserById(idUserClient);
        Address address = addressService.getAddressById(idAddress);
        if(userClient.getAddressList().contains(address)) {
            return measurementRepository.findAllByMeterIn(address, pageable);
        }
        else {
            throw new AddressNotExistsException("The address not exists for this client");
        }
    }*/

    public Page<Measurement> getAllByMeterAndDateBetween(Integer idMeter,Integer idUser, Date from, Date to, Pageable pageable) throws MeterNotExistsException, UserNotExistsException, AccessNotAllowedException {
        Meter meter = meterService.getMeterById(idMeter);
        User user = userService.getUserById(idUser);

        if(userService.containsMeter(user,meter) || user.getTypeUser().equals(TypeUser.EMPLOYEE)) {
            return measurementRepository.findAllByMeterAndDateBetween(meter,from,to,pageable);
        }
        else {
            throw new AccessNotAllowedException("You have not access to this resource");
        }
    }

    public Measurement addMeasurement(Measurement measurement) {
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

    public void addMeterToMeasurement(Integer id, Integer idMeter) throws MeasurementNotExistsException, MeterNotExistsException {
        Meter meter = meterService.getMeterById(idMeter);
        Measurement measurement = getMeasurementById(id);
        measurement.setMeter(meter);
        measurementRepository.save(measurement);
    }

    public void deleteMeasurementById(Integer id) throws MeasurementNotExistsException{
        getMeasurementById(id);
        measurementRepository.deleteById(id);
    }
}
