package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/measurement")
public class MeaurementController {

    @Autowired
    private MeasurementService measurementService;


    @PostMapping("/")
    public PostResponse addMeasurement(@RequestBody Measurement measurement){
       return measurementService.addMeasurement(measurement);
    }

    @GetMapping()
    public List<Measurement> getAllMeasurements(){
        return measurementService.getAllMeasuremets();
    }

    @GetMapping("/{id}")
    public Measurement getMeasurementById(@PathVariable Integer id){
        return measurementService.getMeasurementById(id);
    }

    @DeleteMapping
    public void deleteMeasurementById(@PathVariable Integer id){
        measurementService.deleteMeasurementById(id);
    }

    @PutMapping("/{id}/{idMeter}")
    public void addMeterToMeasurement(@PathVariable Integer id, @PathVariable Integer idMeter){
        measurementService.addMeterToMeasurement(id, idMeter);
    }



}
