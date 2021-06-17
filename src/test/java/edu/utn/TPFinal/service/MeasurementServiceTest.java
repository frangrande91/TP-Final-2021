package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exception.AccessNotAllowedException;
import edu.utn.TPFinal.exception.RestrictDeleteException;
import edu.utn.TPFinal.exception.notFound.AddressNotExistsException;
import edu.utn.TPFinal.exception.notFound.MeasurementNotExistsException;
import edu.utn.TPFinal.exception.notFound.MeterNotExistsException;
import edu.utn.TPFinal.exception.notFound.UserNotExistsException;
import edu.utn.TPFinal.model.*;
import edu.utn.TPFinal.model.dto.ReceivedMeasurementDto;
import edu.utn.TPFinal.repository.MeasurementRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.utn.TPFinal.utils.AddressTestUtils.aAddress;
import static edu.utn.TPFinal.utils.BillTestUtils.aBill;
import static edu.utn.TPFinal.utils.MeasurementTestUtils.*;
import static edu.utn.TPFinal.utils.MeterTestUtils.*;
import static edu.utn.TPFinal.utils.UserTestUtils.aUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MeasurementServiceTest {

    private MeasurementRepository measurementRepository;
    private MeterService meterService;
    private AddressService addressService;
    private UserService userService;

    private MeasurementService measurementService;

    @BeforeEach
    public void setUp(){
        measurementRepository = mock(MeasurementRepository.class);
        meterService = mock(MeterService.class);
        addressService = mock(AddressService.class);
        userService = mock(UserService.class);

        measurementService = new MeasurementService(measurementRepository,meterService,addressService,userService);
    }

    @Test
    public void addMeasurement() {
        try {
            Measurement measurement = Measurement
                                        .builder()
                                        .id(null)
                                        .meter(aMeter())
                                        .bill(null)
                                        .quantityKw(2.0)
                                        .date(LocalDate.of(2020,5,5))
                                        .priceMeasurement(null)
                                        .build();

            when(meterService.getMeterBySerialNumberAndPassword(aMeter().getSerialNumber(),aMeter().getPassword())).thenReturn(aMeter());
            when(measurementRepository.save(measurement)).thenReturn(measurement);

            Measurement measurementSaved = measurementService.addMeasurement(aReceivedMeasurementDto());

            assertEquals(measurement,measurementSaved);

            verify(meterService,times(1)).getMeterBySerialNumberAndPassword(aMeter().getSerialNumber(),aMeter().getPassword());
            verify(measurementRepository,times(1)).save(measurementSaved);

        } catch (MeterNotExistsException e) {
            fail(e);
        }
    }

    @Test
    public void getAllMeasurements() {
        Pageable pageable = PageRequest.of(1,1);
        Mockito.when(measurementRepository.findAll(pageable)).thenReturn(aMeasurementPage());
        Page<Measurement> measurementPage = measurementService.getAllMeasurements(pageable);

        assertEquals(aMeasurementPage().getTotalElements(),measurementPage.getTotalElements());
        assertEquals(aMeasurementPage().getTotalPages(), measurementPage.getTotalElements());
        assertEquals(aMeasurementPage().getContent().size(),measurementPage.getContent().size());

        Mockito.verify(measurementRepository,Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void getAllSpec() {
        Pageable pageable = PageRequest.of(1,1);
        Specification<Measurement> measurementSpecification = specMeasurement(2.0);

        Mockito.when(measurementRepository.findAll(measurementSpecification,pageable)).thenReturn(aMeasurementPage());
        Page<Measurement> measurementPage = measurementService.getAllSpec(measurementSpecification,pageable);

        assertEquals(aMeasurementPage().getTotalElements(),measurementPage.getTotalElements());
        assertEquals(aMeasurementPage().getTotalPages(), measurementPage.getTotalElements());
        assertEquals(aMeasurementPage().getContent().size(),measurementPage.getContent().size());

        Mockito.verify(measurementRepository,Mockito.times(1)).findAll(measurementSpecification,pageable);
    }

    @Test
    public void getAllSort() {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"id"));
        orders.add(new Sort.Order(Sort.Direction.DESC,"quantityKw"));
        Pageable pageable = PageRequest.of(1,1,Sort.by(orders));

        Mockito.when(measurementRepository.findAll(pageable)).thenReturn(aMeasurementPage());
        Page<Measurement> measurementPage = measurementService.getAllSort(1,1,orders);

        assertEquals(aMeasurementPage().getTotalElements(),measurementPage.getTotalElements());
        assertEquals(aMeasurementPage().getTotalPages(), measurementPage.getTotalElements());
        assertEquals(aMeasurementPage().getContent().size(),measurementPage.getContent().size());
        assertEquals(pageable.getSort().toList().size(), orders.size());
        assertEquals(pageable.getSort().toList().get(0), orders.get(0));
        assertEquals(pageable.getSort().toList().get(1), orders.get(1));

        Mockito.verify(measurementRepository,Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void getMeasurementByIdOk() {

        try {
            Integer id = 1;
            Mockito.when(measurementRepository.findById(id)).thenReturn(Optional.of(aMeasurement()));
            Measurement measurement = measurementService.getMeasurementById(1);

            assertEquals(aMeasurement(),measurement);

            Mockito.verify(measurementRepository,Mockito.times(1)).findById(id);
        }
        catch (MeasurementNotExistsException e) {
            fail(e);
        }
    }

    @Test
    public void getMeasurementByIdNotFound() {
        Integer id = 1;
        Mockito.when(measurementRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(MeasurementNotExistsException.class, () -> measurementService.getMeasurementById(id));

        Mockito.verify(measurementRepository, Mockito.times(1)).findById(id);
    }

    @Test
    public void deleteMeasurementByIdOk() {
        try {
            Measurement measurement = Measurement.builder()
                    .id(1)
                    .meter(aMeter())
                    .bill(null)
                    .quantityKw(2.0)
                    .date(LocalDate.of(2021,5,5))
                    .priceMeasurement(100.0)
                    .build();

            when(measurementRepository.findById(1)).thenReturn(Optional.of(measurement));
            doNothing().when(measurementRepository).deleteById(1);

            measurementService.deleteMeasurementById(1);

            verify(measurementRepository, times(1)).findById(1);
            verify(measurementRepository, times(1)).deleteById(1);

        } catch (MeasurementNotExistsException | RestrictDeleteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteMeasurementByIdRestrict() {

        when(measurementRepository.findById(1)).thenReturn(Optional.of(aMeasurement()));
        doNothing().when(measurementRepository).deleteById(1);

        Assertions.assertThrows(RestrictDeleteException.class, ()-> { measurementService.deleteMeasurementById(1); } );

        verify(measurementRepository, times(1)).findById(1);
        verify(measurementRepository, times(0)).deleteById(1);

    }

    @Test
    public void getAllByAddressAndDateBetween() {
        try {
            Pageable pageable = PageRequest.of(1,1);
            when(addressService.getAddressById(1)).thenReturn(aAddress());
            when(measurementRepository.findAllByMeterAndDateBetween(any(),any(),any(),any())).thenReturn(aMeasurementPage());

            Page<Measurement> measurementPage = measurementService.getAllByAddressAndDateBetween(1,LocalDate.of(2021,5,5), LocalDate.of(2021,5,9),pageable);

            assertEquals(aMeasurementPage().getTotalElements(),measurementPage.getTotalElements());
            assertEquals(aMeasurementPage().getTotalPages(), measurementPage.getTotalElements());
            assertEquals(aMeasurementPage().getContent().size(),measurementPage.getContent().size());

            verify(addressService,times(1)).getAddressById(1);
            verify(measurementRepository,times(1)).findAllByMeterAndDateBetween(measurementPage.getContent().get(0).getMeter(),LocalDate.of(2021,5,5), LocalDate.of(2021,5,9),pageable);

        } catch (AddressNotExistsException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllByMeterAndDateBetween() {

        User user = aUser();
        user.setTypeUser(TypeUser.EMPLOYEE);

        try {
            Pageable pageable = PageRequest.of(1,1);
            when(meterService.getMeterById(1)).thenReturn(aMeter());
            when(userService.getUserById(1)).thenReturn(user);
            when(measurementRepository.findAllByMeterAndDateBetween(any(),any(),any(),any())).thenReturn(aMeasurementPage());

            Page<Measurement> measurementPage = measurementService.getAllByMeterAndDateBetween(1,1, LocalDate.of(2021,5,5),LocalDate.of(2021,5,9),pageable);

            assertEquals(aMeasurementPage().getTotalElements(),measurementPage.getTotalElements());
            assertEquals(aMeasurementPage().getTotalPages(), measurementPage.getTotalElements());
            assertEquals(aMeasurementPage().getContent().size(),measurementPage.getContent().size());

            verify(meterService,times(1)).getMeterById(1);
            verify(userService,times(1)).getUserById(1);
            verify(measurementRepository,times(1)).findAllByMeterAndDateBetween(measurementPage.getContent().get(0).getMeter(),LocalDate.of(2021,5,5), LocalDate.of(2021,5,9),pageable);

        } catch (MeterNotExistsException | UserNotExistsException | AccessNotAllowedException e) {
            fail(e);
        }
    }

    @Test
    public void getAllByMeterAndDateBetweenRestrict() {

            Pageable pageable = PageRequest.of(1,1);
            User user = aUser();
            user.setTypeUser(TypeUser.CLIENT);

        try {
            when(meterService.getMeterById(1)).thenReturn(aMeter());
            when(userService.getUserById(1)).thenReturn(user);
            when(measurementRepository.findAllByMeterAndDateBetween(any(),any(),any(),any())).thenReturn(aMeasurementPage());

            assertThrows(AccessNotAllowedException.class,() -> {
                        measurementService.getAllByMeterAndDateBetween(1,1, LocalDate.of(2021,5,5),LocalDate.of(2021,5,9),pageable);
            } );

            verify(meterService,times(1)).getMeterById(1);
            verify(userService,times(1)).getUserById(1);
            verify(measurementRepository,times(0)).findAllByMeterAndDateBetween(aMeter(),LocalDate.of(2021,5,5), LocalDate.of(2021,5,9),pageable);
        } catch (MeterNotExistsException | UserNotExistsException e) {
            fail(e);
        }
    }

    @Test
    public void addMeterToMeasurement() {
        Measurement measurementUpdated = aMeasurement();
        measurementUpdated.setMeter(aMeter());

        try {
            when(meterService.getMeterById(any())).thenReturn(aMeter());
            when(measurementRepository.findById(any())).thenReturn(Optional.of(aMeasurement()));
            when(measurementRepository.save(any())).thenReturn(measurementUpdated);

            Measurement measurement = measurementService.addMeterToMeasurement(aMeasurement().getId(),aMeter().getId());

            assertEquals(aMeter(),measurement.getMeter());

            verify(meterService,times(1)).getMeterById(aMeter().getId());
            verify(measurementRepository,times(1)).findById(aMeasurement().getId());

        } catch (MeterNotExistsException | MeasurementNotExistsException e) {
            e.printStackTrace();
        }
    }

}
