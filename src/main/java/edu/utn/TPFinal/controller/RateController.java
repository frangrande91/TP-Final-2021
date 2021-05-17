package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(value = "/")
    public PostResponse addRate(@RequestBody Rate rate) {
        return rateService.addRate(rate);
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<Rate>> getAllRates() {
        List<Rate> rates = rateService.getAll();
        return response(rates);
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

    private ResponseEntity response(List list){
        return ResponseEntity.status(list.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK).body(list);
    }


}
