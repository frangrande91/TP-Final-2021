package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.RateNotExistsException;
import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
public class RateService {

    private RateRepository rateRepository;

    @Autowired
    public RateService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }


    public Rate addRate(Rate rate) throws SQLIntegrityConstraintViolationException {
        return rateRepository.save(rate);
    }

    public Page<Rate> getAllRates(Pageable pageable) {
        return rateRepository.findAll(pageable);
    }

    public Page<Rate> getAllSpec(Specification<Rate> rateSpecification, Pageable pageable) {
        return rateRepository.findAll(rateSpecification, pageable);
    }

    public Page<Rate> getAllSort(Integer page, Integer size, List<Sort.Order> orders) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        return rateRepository.findAll(pageable);
    }

    public Rate getRateById(Integer id) throws RateNotExistsException {
        return rateRepository.findById(id).orElseThrow(RateNotExistsException::new);
    }


    /*
    public void deleteRate(Rate rate) {
        rateRepository.delete(rate);
    }

    public void deleteRateById(Integer id) {
        rateRepository.deleteById(id);
    }

     */


}
