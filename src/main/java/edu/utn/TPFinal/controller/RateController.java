package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping("/rate")
public class RateController {

    private RateService rateService;

    @Autowired
    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping(value = "/")
    public List<Rate> getAll() {
        return rateService.getAll();
    }

    @GetMapping(value = "/{id}")
    public Rate getByID(@PathVariable Integer id) {
        return rateService.getByID(id);
    }

    @PostMapping(value = "/")
    public Rate addRate(@RequestBody Rate rate) {
        return rateService.addRate(rate);
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
