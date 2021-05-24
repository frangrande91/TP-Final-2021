package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.exceptions.MeasurementNotExistsException;
import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/measurement")
public class MeaurementController {

    private MeasurementService measurementService;

    @Autowired
    public MeaurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @PostMapping("/")
    public PostResponse addMeasurement(@RequestBody Measurement measurement){
       return measurementService.addMeasurement(measurement);
    }

    @GetMapping
    public ResponseEntity getAllMeasurements(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                             @RequestParam(value = "page", defaultValue = "0") Integer page){
        Pageable pageable = PageRequest.of(page, size);
        Page<Measurement> measurementPage = measurementService.getAllMeasurements(pageable);
        return response(measurementPage);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Measurement> getMeasurementById(@PathVariable Integer id) throws MeasurementNotExistsException {
        Measurement measurement = measurementService.getMeasurementById(id);
        return ResponseEntity.ok(measurement);
    }

    @DeleteMapping
    public void deleteMeasurementById(@PathVariable Integer id){
        measurementService.deleteMeasurementById(id);
    }

    @PutMapping("/{id}/{idMeter}")
    public void addMeterToMeasurement(@PathVariable Integer id, @PathVariable Integer idMeter){
        measurementService.addMeterToMeasurement(id, idMeter);
    }

    /*
    private ResponseEntity response(List list){
        return ResponseEntity.status(list.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK).body(list);
    }

     */

    private ResponseEntity response(Page<Measurement> pageMeasurement){
        if(!pageMeasurement.getContent().isEmpty()){
            return ResponseEntity.
                    status(HttpStatus.OK).
                    header("X-Total-Count", Long.toString(pageMeasurement.getTotalElements())).
                    header("X-Total-Pages", Long.toString(pageMeasurement.getTotalPages())).
                    body(pageMeasurement.getContent());
        }
        else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(pageMeasurement.getContent());
        }


    }



}
