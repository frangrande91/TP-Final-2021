package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.service.MeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meter")
public class MeterController {

    @Autowired
    private MeterService meterService;


    @PostMapping("/")
    public void addMeter(@RequestBody Meter meter){
        meterService.addMeter(meter);
    }

    @GetMapping()
    public List<Meter> getAllMeters(){
        return meterService.getAllMeters();
    }

    @GetMapping("/{id}")
    public Meter getMeterById(@PathVariable Integer id){
        return meterService.getMeterById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteMeterById(@PathVariable Integer id){
        meterService.deleteMeterById(id);
    }



}
