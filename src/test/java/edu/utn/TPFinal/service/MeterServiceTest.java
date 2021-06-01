package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.alreadyExists.MeterAlreadyExistsException;
import edu.utn.TPFinal.exceptions.notFound.MeterNotExistsException;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.repository.MeterRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static edu.utn.TPFinal.utils.MeterTestUtils.*;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MeterServiceTest {

    @InjectMocks
    private MeterService meterService;

    @Mock
    private MeterRepository meterRepository;

    @Test
    public void addMeterOk() {
        try {
           Mockito.when(meterRepository.save(aMeter())).thenReturn(aMeter());
           Meter meter = meterService.addMeter(aMeter());
           Mockito.verify(meterRepository,Mockito.times(1)).save(meter);
        }
        catch (MeterAlreadyExistsException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void addMeterAlreadyExists() {
        Meter meter = aMeter();
        Mockito.when(meterRepository.findByIdOrSerialNumber(meter.getId(),meter.getSerialNumber())).thenReturn(meter);
        Assertions.assertThrows(MeterAlreadyExistsException.class, () -> meterService.addMeter(meter));
    }

    @Test
    public void getAllMeters() {
        Pageable pageable = PageRequest.of(1,1);
        Mockito.when(meterRepository.findAll(pageable)).thenReturn(aMeterPage());
        meterService.getAllMeters(pageable);
        Mockito.verify(meterRepository,Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void getAllSpec() {
        Pageable pageable = PageRequest.of(1,1);
        Specification<Meter> meterSpecification = specMeter("12345");
        Mockito.when(meterRepository.findAll(meterSpecification,pageable)).thenReturn(aMeterPage());
        meterService.getAllSpec(meterSpecification,pageable);
        Mockito.verify(meterRepository,Mockito.times(1)).findAll(meterSpecification,pageable);
    }

    @Test
    public void getAllSort() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.DESC,"id"));
        orders.add(new Order(Sort.Direction.DESC,"serialNumber"));
        Pageable pageable = PageRequest.of(1,1,Sort.by(orders));
        Mockito.when(meterRepository.findAll(pageable)).thenReturn(aMeterPage());
        meterService.getAllSort(1,1,orders);
        Mockito.verify(meterRepository,Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void getMeterByIdOk() {

        try {
            Integer id = 1234;
            Mockito.when(meterRepository.findById(id)).thenReturn(Optional.of(aMeter()));
            meterService.getMeterById(id);
            Mockito.verify(meterRepository,Mockito.times(1)).findById(id);
        }
        catch (MeterNotExistsException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void getMeterByIdNotFound() {
            Integer id = 1234;
            Mockito.when(meterRepository.findById(id)).thenReturn(Optional.empty());
            Assertions.assertThrows(MeterNotExistsException.class, () -> meterService.getMeterById(id));
    }

    @Test
    public void deleteMeterByIdOk() {
        Integer id = 1234;
        try {
            Mockito.when(meterRepository.findById(id)).thenReturn(Optional.of(aMeter()));
            meterService.getMeterById(id);
            meterRepository.deleteById(id);
            Mockito.doNothing().when(meterRepository).deleteById(id);
            meterService.deleteMeterById(id);
        } catch (MeterNotExistsException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteMeterByIdNotFound() {
        Integer id = 1234;
        Mockito.when(meterRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(MeterNotExistsException.class, ()-> {
            meterService.getMeterById(id);
            meterService.deleteMeterById(id);
            }
        );
    }

}