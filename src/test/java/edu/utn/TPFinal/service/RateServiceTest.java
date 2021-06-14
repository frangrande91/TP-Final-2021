package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exception.alreadyExists.RateAlreadyExists;
import edu.utn.TPFinal.exception.notFound.RateNotExistsException;
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

import static edu.utn.TPFinal.utils.MeterTestUtils.aMeterPage;
import static edu.utn.TPFinal.utils.RateTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


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

    /*
    @Test
    public void deleteRateByIdOk() {
        Integer id = 1234;
        try {
            Mockito.when(rateRepository.findById(id)).thenReturn(Optional.of(aRate()));
            Mockito.doNothing().when(rateRepository).deleteById(id);

            rateService.deleteRateById(id);

            Mockito.verify(rateRepository,Mockito.times(1)).findById(id);
            Mockito.verify(rateRepository,Mockito.times(1)).deleteById(id);
        }
        catch (RateNotExistsException ex) {
            fail(ex);
        }
    }

     */

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
}
