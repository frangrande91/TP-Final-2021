package edu.utn.TPFinal.service;
import edu.utn.TPFinal.exceptions.alreadyExists.MeterAlreadyExistsException;
import edu.utn.TPFinal.exceptions.notFound.MeterNotExistsException;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.repository.MeterRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import static edu.utn.TPFinal.utils.MeterTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MeterServiceTest {

    private static MeterService meterService;
    private static MeterRepository meterRepository;


    @BeforeAll
    public static void setUp() {
        meterRepository = Mockito.mock(MeterRepository.class);
        meterService = new MeterService(meterRepository);
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
    public void deleteMeterByIdOk() {
        Integer id = 1234;
        try {

            Mockito.when(meterRepository.findById(id)).thenReturn(Optional.of(aMeter()));
            Mockito.doNothing().when(meterRepository).deleteById(id);

            meterService.deleteMeterById(id);

            Mockito.verify(meterRepository, Mockito.times(1)).findById(id);
            Mockito.verify(meterRepository, Mockito.times(1)).deleteById(id);

        } catch (MeterNotExistsException e) {
            fail(e);
        }
    }

    @Test
    public void deleteMeterByIdNotFound() {
        Integer id = 1234;
        Mockito.when(meterRepository.findById(id)).thenReturn(Optional.empty());
        Mockito.doNothing().when(meterRepository).deleteById(id);

        Assertions.assertThrows(MeterNotExistsException.class, ()-> { meterService.deleteMeterById(id); } );

        Mockito.verify(meterRepository,Mockito.times(1)).findById(id);
    }

}