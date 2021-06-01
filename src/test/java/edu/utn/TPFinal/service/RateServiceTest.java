package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.alreadyExists.RateAlreadyExists;
import edu.utn.TPFinal.exceptions.notFound.RateNotExistsException;
import edu.utn.TPFinal.model.Rate;

import edu.utn.TPFinal.repository.RateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.utn.TPFinal.utils.RateTestUtils.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RateServiceTest {

    @InjectMocks
    private RateService rateService;

    @Mock
    private RateRepository rateRepository;

    @Test
    public void addRateOk() {
        try {
            Mockito.when(rateRepository.save(aRate())).thenReturn(aRate());
            Rate rate = rateService.addRate(aRate());
            Mockito.verify(rateRepository,Mockito.times(1)).save(rate);
        }
        catch (RateAlreadyExists ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void addRateAlreadyExists() {
        Rate rate = aRate();
        Mockito.when(rateRepository.findByIdOrTypeRate(rate.getId(),rate.getTypeRate())).thenReturn(aRate());
        Assertions.assertThrows(RateAlreadyExists.class,()-> rateService.addRate(rate));
    }

    @Test
    public void getAllRates() {
        Pageable pageable = PageRequest.of(1,1);
        Mockito.when(rateRepository.findAll(pageable)).thenReturn(aRatePage());
        rateService.getAllRates(pageable);
        Mockito.verify(rateRepository,Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void getAllSpec() {
        Pageable pageable = PageRequest.of(1,1);
        Specification<Rate> rateSpecification = specRates(300.00);
        Mockito.when(rateRepository.findAll(rateSpecification,pageable)).thenReturn(aRatePage());
        rateService.getAllSpec(rateSpecification,pageable);
        Mockito.verify(rateRepository,Mockito.times(1)).findAll(rateSpecification,pageable);
    }

    @Test
    public void getAllSort() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.DESC,"id"));
        orders.add(new Order(Sort.Direction.DESC,"value"));
        Pageable pageable = PageRequest.of(1,1, Sort.by(orders));
        Mockito.when(rateRepository.findAll(pageable)).thenReturn(aRatePage());
        rateService.getAllSort(1,1,orders);
        Mockito.verify(rateRepository,Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void getRateByIdOk() {

        try {
            Integer id = 1234;
            Mockito.when(rateRepository.findById(id)).thenReturn(Optional.of(aRate()));
            rateService.getRateById(id);
            Mockito.verify(rateRepository,Mockito.times(1)).findById(id);
        }
        catch (RateNotExistsException ex) {
            ex.printStackTrace();
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
        try {
            Mockito.when(rateRepository.findById(id)).thenReturn(Optional.of(aRate()));
            rateService.getRateById(id);
            rateRepository.deleteById(id);
            Mockito.doNothing().when(rateRepository).deleteById(id);
            rateService.deleteRateById(id);
        }
        catch (RateNotExistsException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void deleteRateByIdNotFound() {
        Integer id = 1234;
        Mockito.when(rateRepository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrows(RateNotExistsException.class, () -> {
            rateService.getRateById(id);
            rateService.deleteRateById(id);
        });
    }
}
