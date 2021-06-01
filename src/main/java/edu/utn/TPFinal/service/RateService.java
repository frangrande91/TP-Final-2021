package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.alreadyExists.MeterAlreadyExistsException;
import edu.utn.TPFinal.exceptions.alreadyExists.RateAlreadyExists;
import edu.utn.TPFinal.exceptions.notFound.ModelNotExistsException;
import edu.utn.TPFinal.exceptions.notFound.RateNotExistsException;
import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class RateService {

    private RateRepository rateRepository;

    @Autowired
    public RateService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public Rate addRate(Rate rate) throws RateAlreadyExists {
        if(isNull(rateRepository.findByIdOrTypeRate(rate.getId(),rate.getTypeRate()))) {
            return rateRepository.save(rate);
        }
        else {
            throw new RateAlreadyExists("Rate already exists");
        }
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
        return rateRepository.findById(id).orElseThrow(() -> new RateNotExistsException("Rate not exists"));
    }

    public void deleteRateById(Integer id) throws RateNotExistsException{
        getRateById(id);
        rateRepository.deleteById(id);
    }

}
