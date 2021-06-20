package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exception.RestrictDeleteException;
import edu.utn.TPFinal.exception.ViolationChangeKeyAttributeException;
import edu.utn.TPFinal.exception.alreadyExists.MeterAlreadyExistsException;
import edu.utn.TPFinal.exception.notFound.MeterNotExistsException;
import edu.utn.TPFinal.exception.notFound.ModelNotExistsException;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.repository.MeterRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.utn.TPFinal.utils.AddressTestUtils.aAddress;
import static edu.utn.TPFinal.utils.MeterTestUtils.*;
import static edu.utn.TPFinal.utils.ModelTestUtils.aModel;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MeterServiceTest {

    private static MeterService meterService;
    private static MeterRepository meterRepository;
    private static ModelService modelService;


    @BeforeAll
    public static void setUp() {
        meterRepository = mock(MeterRepository.class);
        modelService = mock(ModelService.class);
        meterService = new MeterService(meterRepository, modelService);

    }

    @AfterEach
    public void after() {
        Mockito.reset(meterRepository);
    }

    @Test
    public void addMeterOk() {
        try {
           Mockito.when(meterRepository.findByIdOrSerialNumber(aMeter().getId(),aMeter().getSerialNumber())).thenReturn(null);
           Mockito.when(meterRepository.save(aMeter())).thenReturn(aMeter());
           Meter meter = meterService.addMeter(aMeter());

           assertEquals(aMeter().getId(),meter.getId());
           assertEquals(aMeter().getSerialNumber(),meter.getSerialNumber());
           assertEquals(aMeter().getModel(),meter.getModel());
           assertEquals(aMeter().getPassword(),meter.getPassword());

           Mockito.verify(meterRepository,Mockito.times(1)).findByIdOrSerialNumber(meter.getId(),meter.getSerialNumber());
           Mockito.verify(meterRepository,Mockito.times(1)).save(meter);
        }
        catch (MeterAlreadyExistsException ex) {
            fail(ex);
        }
    }

    @Test
    public void addMeterAlreadyExists() {
        Meter meter = aMeter();
        Mockito.when(meterRepository.findByIdOrSerialNumber(meter.getId(),meter.getSerialNumber())).thenReturn(meter);

        Assertions.assertThrows(MeterAlreadyExistsException.class, () -> meterService.addMeter(meter));

        Mockito.verify(meterRepository,Mockito.times(1)).findByIdOrSerialNumber(meter.getId(),meter.getSerialNumber());
        Mockito.verify(meterRepository,Mockito.times(0)).save(meter);
    }

    @Test
    public void getAllMeters() {
        Pageable pageable = PageRequest.of(1,1);
        Mockito.when(meterRepository.findAll(pageable)).thenReturn(aMeterPage());
        Page<Meter> meterPage = meterService.getAllMeters(pageable);

        assertEquals(aMeterPage().getTotalElements(),meterPage.getTotalElements());
        assertEquals(aMeterPage().getTotalPages(), meterPage.getTotalElements());
        assertEquals(aMeterPage().getContent().size(),meterPage.getContent().size());

        Mockito.verify(meterRepository,Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void getAllSpec() {
        Pageable pageable = PageRequest.of(1,1);
        Specification<Meter> meterSpecification = specMeter("12345");

        Mockito.when(meterRepository.findAll(meterSpecification,pageable)).thenReturn(aMeterPage());
        Page<Meter> meterPage = meterService.getAllSpec(meterSpecification,pageable);

        assertEquals(aMeterPage().getTotalElements(),meterPage.getTotalElements());
        assertEquals(aMeterPage().getTotalPages(), meterPage.getTotalElements());
        assertEquals(aMeterPage().getContent().size(),meterPage.getContent().size());

        Mockito.verify(meterRepository,Mockito.times(1)).findAll(meterSpecification,pageable);
    }

    @Test
    public void getAllSort() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.DESC,"id"));
        orders.add(new Order(Sort.Direction.DESC,"serialNumber"));
        Pageable pageable = PageRequest.of(1,1,Sort.by(orders));

        Mockito.when(meterRepository.findAll(pageable)).thenReturn(aMeterPage());
        Page<Meter> meterPage = meterService.getAllSort(1,1,orders);

        assertEquals(aMeterPage().getTotalElements(),meterPage.getTotalElements());
        assertEquals(aMeterPage().getTotalPages(), meterPage.getTotalElements());
        assertEquals(aMeterPage().getContent().size(),meterPage.getContent().size());
        assertEquals(pageable.getSort().toList().size(), orders.size());
        assertEquals(pageable.getSort().toList().get(0), orders.get(0));
        assertEquals(pageable.getSort().toList().get(1), orders.get(1));

        Mockito.verify(meterRepository,Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void getMeterByIdOk() {

        try {
            Integer id = 1234;
            Mockito.when(meterRepository.findById(id)).thenReturn(Optional.of(aMeter()));
            Meter meter = meterService.getMeterById(id);

            assertEquals(aMeter().getId(),meter.getId());
            assertEquals(aMeter().getSerialNumber(),meter.getSerialNumber());
            assertEquals(aMeter().getPassword(),meter.getPassword());
            assertEquals(aMeter().getModel(),meter.getModel());

            Mockito.verify(meterRepository,Mockito.times(1)).findById(id);
        }
        catch (MeterNotExistsException ex) {
            fail(ex);
        }
    }

    @Test
    public void getMeterByIdNotFound() {
            Integer id = 1234;
            Mockito.when(meterRepository.findById(id)).thenReturn(Optional.empty());

            Assertions.assertThrows(MeterNotExistsException.class, () -> meterService.getMeterById(id));

            Mockito.verify(meterRepository,Mockito.times(1)).findById(id);
    }

    @Test
    public void getMeterBySerialNumberAndPasswordOk() {

        try {
            Integer id = 1234;
            Mockito.when(meterRepository.findBySerialNumberAndPassword(any(),any())).thenReturn(Optional.of(aMeter()));
            Meter meter = meterService.getMeterBySerialNumberAndPassword(aMeter().getSerialNumber(),aMeter().getPassword());

            assertEquals(aMeter().getId(),meter.getId());
            assertEquals(aMeter().getSerialNumber(),meter.getSerialNumber());
            assertEquals(aMeter().getPassword(),meter.getPassword());
            assertEquals(aMeter().getModel(),meter.getModel());

            Mockito.verify(meterRepository,Mockito.times(1)).findBySerialNumberAndPassword(aMeter().getSerialNumber(),aMeter().getPassword());
        }
        catch (MeterNotExistsException ex) {
            fail(ex);
        }
    }

    @Test
    public void getMeterBySerialNumberAndPasswordNotFound() {
        Integer id = 1234;
        Mockito.when(meterRepository.findBySerialNumberAndPassword(any(),any())).thenReturn(Optional.empty());

        Assertions.assertThrows(MeterNotExistsException.class, () -> meterService.getMeterBySerialNumberAndPassword(aMeter().getSerialNumber(),aMeter().getPassword()));

        Mockito.verify(meterRepository,Mockito.times(1)).findBySerialNumberAndPassword(aMeter().getSerialNumber(),aMeter().getPassword());
    }


    @Test
    public void deleteMeterByIdOk() {
        Integer id = 1234;
        try {

            Mockito.when(meterRepository.findById(id)).thenReturn(Optional.of(aMeter()));
            Mockito.doNothing().when(meterRepository).deleteById(id);

            meterService.deleteMeterById(id);

            Mockito.verify(meterRepository, Mockito.times(1)).findById(id);
            Mockito.verify(meterRepository, Mockito.times(1)).deleteById(id);

        } catch (MeterNotExistsException | RestrictDeleteException e) {
            fail(e);
        }
    }

    @Test
    public void deleteMeterByIdRestrict() {

        Meter meter = aMeter();
        meter.setAddress(aAddress());
        Mockito.when(meterRepository.findById(any())).thenReturn(Optional.of(meter));
        Mockito.doNothing().when(meterRepository).deleteById(meter.getId());

        Assertions.assertThrows(RestrictDeleteException.class, ()-> { meterService.deleteMeterById(meter.getId()); } );

        Mockito.verify(meterRepository,Mockito.times(1)).findById(meter.getId());
    }

    @Test
    public void updateMeterOk() {
        Meter meter = aMeter();
        meter.setSerialNumber("123456");
        try {
            when(meterRepository.findById(1)).thenReturn(Optional.of(meter));
            when(meterRepository.save(meter)).thenReturn(meter);

            Meter meterUpdated = meterService.updateMeter(meter.getId(),meter);

            assertEquals(meter,meterUpdated);

            verify(meterRepository, times(1)).save(meter);
        }
        catch (ViolationChangeKeyAttributeException | MeterNotExistsException e){
            fail(e);
        }
    }

    @Test
    public void updateMeterViolationKey() {
        Meter meter = aMeter();
        meter.setSerialNumber("123456");
        meter.setId(55);

        Meter meter2 = aMeter();
        meter2.setSerialNumber("123456");
        meter2.setId(56);

        when(meterRepository.findById(any())).thenReturn(Optional.of(meter));
        when(meterRepository.save(meter)).thenReturn(meter);

        Assertions.assertThrows(ViolationChangeKeyAttributeException.class, () -> {
            meterService.updateMeter(meter.getId(),meter2);
        });

        verify(meterRepository, times(0)).save(meter);
    }

    @Test
    public void updateMeterViolationKey2() {
        Meter meter = aMeter();
        meter.setSerialNumber("123456");


        Meter meter2 = aMeter();
        meter2.setSerialNumber("1234567");


        when(meterRepository.findById(any())).thenReturn(Optional.of(meter));
        when(meterRepository.save(meter)).thenReturn(meter);

        Assertions.assertThrows(ViolationChangeKeyAttributeException.class, () -> {
            meterService.updateMeter(meter.getId(),meter2);
        });

        verify(meterRepository, times(0)).save(meter);
    }

    @Test
    public void addModelToMeterOk(){
        try{
            Meter meterAux = aMeter();
            meterAux.setModel(aModel());
            when(meterRepository.findById(aMeter().getId())).thenReturn(Optional.of(aMeter()));
            when(modelService.getModelById(aModel().getId())).thenReturn(aModel());
            when(meterRepository.save(aMeter())).thenReturn(aMeter());

            Meter meter = meterService.addModelToMeter(1, 1);

            assertEquals(aMeter().getModel(), meter.getModel());
            verify(meterRepository, times(1)).findById(aModel().getId());
            verify(modelService, times(1)).getModelById(1);
            verify(meterRepository, times(1)).save(aMeter());
        } catch (ModelNotExistsException | MeterNotExistsException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void addModelToMeterMeterNotExist(){
        when(meterRepository.findById(aAddress().getId())).thenReturn(Optional.empty());

        assertThrows(MeterNotExistsException.class, () -> meterService.addModelToMeter(aAddress().getId(),aMeter().getId()) );
        verify(meterRepository, times(1)).findById(aMeter().getId());
    }


}