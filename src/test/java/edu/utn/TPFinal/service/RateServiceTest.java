package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exception.RestrictDeleteException;
import edu.utn.TPFinal.exception.ViolationChangeKeyAttributeException;
import edu.utn.TPFinal.exception.alreadyExists.RateAlreadyExists;
import edu.utn.TPFinal.exception.notFound.AddressNotExistsException;
import edu.utn.TPFinal.exception.notFound.BillNotExistsException;
import edu.utn.TPFinal.exception.notFound.RateNotExistsException;
import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.Rate;

import edu.utn.TPFinal.repository.RateRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.utn.TPFinal.utils.AddressTestUtils.aAddress;
import static edu.utn.TPFinal.utils.BillTestUtils.aBill;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeterPage;
import static edu.utn.TPFinal.utils.RateTestUtils.*;
import static edu.utn.TPFinal.utils.RateTestUtils.aRate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class RateServiceTest {


    private static RateService rateService;
    private static RateRepository rateRepository;

    @BeforeAll
    public static void setUp() {
        rateRepository = Mockito.mock(RateRepository.class);
        rateService = new RateService(rateRepository);
    }


    @AfterEach
    public void after() {
        Mockito.reset(rateRepository);
    }

    @Test
    public void addRateOk() {
        try {
            Mockito.when(rateRepository.findByIdOrTypeRate(aRate().getId(),aRate().getTypeRate())).thenReturn(null);
            Mockito.when(rateRepository.save(aRate())).thenReturn(aRate());
            Rate rate = rateService.addRate(aRate());

            assertEquals(aRate().getId(),rate.getId());
            assertEquals(aRate().getValue(),rate.getValue());
            assertEquals(aRate().getTypeRate(),rate.getTypeRate());

            Mockito.verify(rateRepository,Mockito.times(1)).save(aRate());
        }
        catch (RateAlreadyExists ex) {
            fail(ex);
        }
    }

    @Test
    public void addRateAlreadyExists() {
        Rate rate = aRate();
        Mockito.when(rateRepository.findByIdOrTypeRate(rate.getId(),rate.getTypeRate())).thenReturn(aRate());

        Assertions.assertThrows(RateAlreadyExists.class,()-> rateService.addRate(rate));

        Mockito.verify(rateRepository, Mockito.times(1)).findByIdOrTypeRate(rate.getId(),rate.getTypeRate());
    }

    @Test
    public void getAllRates() {
        Pageable pageable = PageRequest.of(1,1);
        Mockito.when(rateRepository.findAll(pageable)).thenReturn(aRatePage());
        Page<Rate> ratePage = rateService.getAllRates(pageable);

        assertEquals(aMeterPage().getTotalElements(),ratePage.getTotalElements());
        assertEquals(aMeterPage().getTotalPages(), ratePage.getTotalElements());
        assertEquals(aMeterPage().getContent().size(),ratePage.getContent().size());

        Mockito.verify(rateRepository,Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void getAllSpec() {
        Pageable pageable = PageRequest.of(1,1);
        Specification<Rate> rateSpecification = specRates(300.00);


        Mockito.when(rateRepository.findAll(rateSpecification,pageable)).thenReturn(aRatePage());
        Page<Rate> ratePage = rateService.getAllSpec(rateSpecification,pageable);

        assertEquals(aRatePage().getTotalElements(),ratePage.getTotalElements());
        assertEquals(aRatePage().getTotalPages(), ratePage.getTotalElements());
        assertEquals(aRatePage().getContent().size(),ratePage.getContent().size());

        Mockito.verify(rateRepository,Mockito.times(1)).findAll(rateSpecification,pageable);
    }

    @Test
    public void getAllSort() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.DESC,"id"));
        orders.add(new Order(Sort.Direction.DESC,"value"));
        Pageable pageable = PageRequest.of(1,1, Sort.by(orders));

        Mockito.when(rateRepository.findAll(pageable)).thenReturn(aRatePage());
        Page<Rate> ratePage = rateService.getAllSort(1,1,orders);

        assertEquals(aRatePage().getTotalElements(),ratePage.getTotalElements());
        assertEquals(aRatePage().getTotalPages(), ratePage.getTotalElements());
        assertEquals(aRatePage().getContent().size(),ratePage.getContent().size());
        assertEquals(pageable.getSort().toList().get(0), orders.get(0));
        assertEquals(pageable.getSort().toList().get(1), orders.get(1));

        Mockito.verify(rateRepository,Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void getRateByIdOk() {

        try {
            Integer id = 1234;
            Mockito.when(rateRepository.findById(id)).thenReturn(Optional.of(aRate()));
            Rate rate = rateService.getRateById(id);

            assertEquals(aRate().getId(),rate.getId());
            assertEquals(aRate().getValue(),rate.getValue());
            assertEquals(aRate().getTypeRate(),rate.getTypeRate());

            Mockito.verify(rateRepository,Mockito.times(1)).findById(id);
        }
        catch (RateNotExistsException ex) {
            fail(ex);
        }
    }

    @Test
    public void getRateByIdNotFound() {
        Integer id = 1234;
        Mockito.when(rateRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(RateNotExistsException.class, () -> rateService.getRateById(id));
    }

    @Test
    public void deleteRateByIdOk() {
        Integer id = 1234;
        Rate rate = aRate();
        rate.setAddressList(new ArrayList<>());

        try {
            Mockito.when(rateRepository.findById(id)).thenReturn(Optional.of(rate));
            Mockito.doNothing().when(rateRepository).deleteById(id);

            rateService.deleteRateById(id);

            Mockito.verify(rateRepository,Mockito.times(1)).findById(id);
            Mockito.verify(rateRepository,Mockito.times(1)).deleteById(id);
        }
        catch (RateNotExistsException | RestrictDeleteException ex) {
            fail(ex);
        }
    }

    @Test
    public void deleteRateByIdRestrict() {
        Integer id = 1234;
        Rate rate = aRate();
        rate.setAddressList(new ArrayList<>());
        rate.getAddressList().add(aAddress());

        Mockito.when(rateRepository.findById(id)).thenReturn(Optional.of(rate));
        Mockito.doNothing().when(rateRepository).deleteById(id);

        assertThrows(RestrictDeleteException.class, ()-> rateService.deleteRateById(id));


        Mockito.verify(rateRepository,Mockito.times(1)).findById(id);
        Mockito.verify(rateRepository,Mockito.times(0)).deleteById(id);

    }

    @Test
    public void deleteRateByIdNotFound() {
        Integer id = 1234;
        Mockito.when(rateRepository.findById(id)).thenReturn(Optional.empty());
        Mockito.doNothing().when(rateRepository).deleteById(id);

        Assertions.assertThrows(RateNotExistsException.class, () -> {
            rateService.deleteRateById(id);
        });

        Mockito.verify(rateRepository,Mockito.times(1)).findById(id);
    }

    @Test
    public void updateAddressOk() {
        try {
            when(rateRepository.findById(1)).thenReturn(Optional.of(aRate()));
            when(rateRepository.save(aRate())).thenReturn(aRate());

            Rate rate = rateService.updateRate(aRate().getId(),aRate());

            assertEquals(aRate(),rate);

            verify(rateRepository, times(1)).save(rate);
        }
        catch (ViolationChangeKeyAttributeException | RateNotExistsException e){
            fail(e);
        }
    }

    @Test
    public void updateAddressViolationKey() {
        Rate rate1 = aRate();
        rate1.setTypeRate("CC");

        when(rateRepository.findById(1)).thenReturn(Optional.of(aRate()));
        when(rateRepository.save(aRate())).thenReturn(aRate());

        Assertions.assertThrows(ViolationChangeKeyAttributeException.class, () -> {
            rateService.updateRate(aRate().getId(),rate1);
        });

        verify(rateRepository, times(0)).save(rate1);
    }

    @Test
    public void updateAddressViolationKey2() {
        Rate rate1 = aRate();
        rate1.setId(21345);

        when(rateRepository.findById(1)).thenReturn(Optional.of(aRate()));
        when(rateRepository.save(aRate())).thenReturn(aRate());

        Assertions.assertThrows(ViolationChangeKeyAttributeException.class, () -> {
            rateService.updateRate(aRate().getId(),rate1);
        });

        verify(rateRepository, times(0)).save(rate1);
    }


}
