package edu.utn.TPFinal.service;

import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class RateService {

    private RateRepository rateRepository;

    @Autowired
    public RateService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public List<Rate> getAll() {
        return rateRepository.findAll();
    }

    public Rate getByID(Integer id) {
        return rateRepository.findById(id).orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND,String.format("The rate with ID: %d",id,"do not exists")));
    }

    public Rate addRate(Rate rate) {
        return rateRepository.save(rate);
    }

    public void deleteRate(Rate rate) {
        rateRepository.delete(rate);
    }

    public void deleteRateById(Integer id) {
        rateRepository.deleteById(id);
    }


}
