package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.service.RateService;
import edu.utn.TPFinal.utils.EntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rate")
public class RateController {

    private RateService rateService;

    @Autowired
    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @PostMapping(value = "/")
    public PostResponse addRate(@RequestBody Rate rate) {
        return rateService.addRate(rate);
    }

    @GetMapping
    public ResponseEntity getAllRates(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Rate> ratePage = rateService.getAllRates(pageable);
        return EntityResponse.response(ratePage);
    }

    @GetMapping(value = "/{id}")
    public Rate getRateByID(@PathVariable Integer id) {
        return rateService.getByID(id);
    }

    @DeleteMapping("/")
    public void deleteRate(@RequestBody Rate rate) {
        rateService.deleteRate(rate);
    }

    @DeleteMapping("/{id}")
    public void deleteRateById(@PathVariable Integer id) {
        rateService.deleteRateById(id);
    }
}
