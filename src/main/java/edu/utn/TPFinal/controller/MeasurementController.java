package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.exceptions.MeasurementNotExistsException;
import edu.utn.TPFinal.exceptions.MeterNotExistsException;
import edu.utn.TPFinal.model.Dto.MeasurementDto;
import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.model.Responses.Response;
import edu.utn.TPFinal.service.MeasurementService;
import edu.utn.TPFinal.utils.EntityResponse;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/measurement")
public class MeasurementController {

    private MeasurementService measurementService;
    private ConversionService conversionService;
    private static final String MEASUREMENT_PATH = "measurement";


    @Autowired
    public MeasurementController(MeasurementService measurementService,ConversionService conversionService) {
        this.measurementService = measurementService;
        this.conversionService = conversionService;
    }

    @PostMapping("/")
    public ResponseEntity<Response> addMeasurement(@RequestBody Measurement measurement) {
        Measurement measurementCreated = measurementService.addMeasurement(measurement);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(EntityURLBuilder.buildURL2(MEASUREMENT_PATH,measurementCreated.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Response.builder().message("The measurement has been created").build());
    }

    @GetMapping
    public ResponseEntity<List<MeasurementDto>> getAllMeasurements(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                             @RequestParam(value = "page", defaultValue = "0") Integer page){
        Pageable pageable = PageRequest.of(page, size);
        Page<Measurement> measurementPage = measurementService.getAllMeasurements(pageable);
        Page<MeasurementDto> measurementDtoPage = measurementPage.map(measurement -> conversionService.convert(measurement,MeasurementDto.class));
        return EntityResponse.response(measurementDtoPage);
    }

    @GetMapping("/spec")
    public ResponseEntity<List<MeasurementDto>> getAllSpec(
            @And({
                    @Spec(path = "model", spec = Equal.class)
            }) Specification<Measurement> measurementSpecification, Pageable pageable ){
        Page<Measurement> measurementPage = measurementService.getAllSpec(measurementSpecification,pageable);
        Page<MeasurementDto> measurementDtoPage = measurementPage.map(measurement -> conversionService.convert(measurement,MeasurementDto.class));
        return EntityResponse.response(measurementDtoPage);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<MeasurementDto>> getAllSorted(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                       @RequestParam String field1, @RequestParam String field2) {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.DESC,field1));
        orders.add(new Order(Sort.Direction.DESC,field2));
        Page<Measurement> measurementPage = measurementService.getAllSort(page,size,orders);
        Page<MeasurementDto> measurementDtoPage = measurementPage.map(measurement -> conversionService.convert(measurement,MeasurementDto.class));
        return EntityResponse.response(measurementDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeasurementDto> getMeasurementById(@PathVariable Integer id) throws MeasurementNotExistsException {
        MeasurementDto measurementDto = conversionService.convert(measurementService.getMeasurementById(id),MeasurementDto.class);
        return ResponseEntity.ok(measurementDto);
    }

    @PutMapping("/{id}/{idMeter}")
    public ResponseEntity<Response> addMeterToMeasurement(@PathVariable Integer id, @PathVariable Integer idMeter) throws MeasurementNotExistsException, MeterNotExistsException {
        measurementService.addMeterToMeasurement(id, idMeter);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Response.builder().message("The Measurement has been modified").build());
    }

    /*
    @DeleteMapping
    public void deleteMeasurementById(@PathVariable Integer id){
        measurementService.deleteMeasurementById(id);
    }
    */



}
