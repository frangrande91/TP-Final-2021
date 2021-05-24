package edu.utn.TPFinal.service;

import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.repository.RateRepository;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class RateService {

    private static final String RATE_PATH = "rate";
    private RateRepository rateRepository;

    @Autowired
    public RateService(RateRepository rateRepository) {
        this.rateRepository = rateRepository;
    }

    public PostResponse addRate(Rate rate) {
        Rate r = rateRepository.save(rate);
        return PostResponse
                .builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(RATE_PATH, r.getId()))
                .build();
    }


    public Page<Rate> getAllRates(Pageable pageable) {
        return rateRepository.findAll(pageable);
    }

    public Rate getByID(Integer id) {
        return rateRepository.findById(id).orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND,String.format("The rate with ID: %d",id,"do not exists")));
    }

    public void deleteRate(Rate rate) {
        rateRepository.delete(rate);
    }

    public void deleteRateById(Integer id) {
        rateRepository.deleteById(id);
    }


}
