package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.service.RateService;
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
        return response(ratePage);
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


    private ResponseEntity response(Page<Rate> pageRate){
        if(!pageRate.getContent().isEmpty()){
            return ResponseEntity.
                    status(HttpStatus.OK).
                    header("X-Total-Count", Long.toString(pageRate.getTotalElements())).
                    header("X-Total-Pages", Long.toString(pageRate.getTotalPages())).
                    body(pageRate.getContent());
        }
        else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(pageRate.getContent());
        }

    }

}
