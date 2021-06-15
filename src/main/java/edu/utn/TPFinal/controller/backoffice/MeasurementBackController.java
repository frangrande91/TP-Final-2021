package edu.utn.TPFinal.controller.backoffice;

import edu.utn.TPFinal.exception.RestrictDeleteException;
import edu.utn.TPFinal.exception.notFound.AddressNotExistsException;
import edu.utn.TPFinal.exception.notFound.MeasurementNotExistsException;
import edu.utn.TPFinal.exception.notFound.MeterNotExistsException;
import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.model.dto.MeasurementDto;
import edu.utn.TPFinal.model.dto.ReceivedMeasurementDto;
import edu.utn.TPFinal.model.dto.UserDto;
import edu.utn.TPFinal.model.response.Response;
import edu.utn.TPFinal.service.MeasurementService;
import edu.utn.TPFinal.utils.EntityResponse;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import lombok.extern.slf4j.Slf4j;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/backoffice/measurements")
@Slf4j
public class MeasurementBackController {

    private MeasurementService measurementService;
    private ConversionService conversionService;
    private static final String MEASUREMENT_PATH = "measurements";

    @Autowired
    public MeasurementBackController(MeasurementService measurementService, ConversionService conversionService) {
        this.measurementService = measurementService;
        this.conversionService = conversionService;
    }


    /**PUNTO 6+*/
    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping("addresses/{idAddress}")
    public ResponseEntity<List<MeasurementDto>> getByAddressForDateRange(@PathVariable Integer idAddress,
                                                                  @RequestParam(value = "from", defaultValue = "2020-12-05") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                                                  @RequestParam(value = "to", defaultValue = "2020-01-05") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
                                                                  @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                  @RequestParam(value = "page", defaultValue = "0") Integer page/*,
                                                                  Authentication authentication*/) throws AddressNotExistsException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Measurement> measurementPage = measurementService.getAllByAddressAndDateBetween(idAddress, from, to, pageable);
        Page<MeasurementDto> measurementDtoPage = measurementPage.map(measurement -> conversionService.convert(measurement,MeasurementDto.class));
        return EntityResponse.listResponse(measurementDtoPage);
    }

    @PostMapping("/")
    public ResponseEntity<Response> addMeasurement(@RequestBody ReceivedMeasurementDto receivedMeasurementDto) throws MeterNotExistsException {

        Measurement measurement = measurementService.addMeasurement(receivedMeasurementDto);
        log.info(receivedMeasurementDto.toString());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(EntityURLBuilder.buildURL2(MEASUREMENT_PATH,measurement.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(EntityResponse.messageResponse("The measurement has been created"));
    }

    @GetMapping("/")
    public ResponseEntity<List<MeasurementDto>> getAllMeasurements(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                   @RequestParam(value = "page", defaultValue = "0") Integer page){
        Pageable pageable = PageRequest.of(page, size);
        Page<Measurement> measurementPage = measurementService.getAllMeasurements(pageable);
        Page<MeasurementDto> measurementDtoPage = measurementPage.map(measurement -> conversionService.convert(measurement,MeasurementDto.class));
        return EntityResponse.listResponse(measurementDtoPage);
    }

    @GetMapping("/spec")
    public ResponseEntity<List<MeasurementDto>> getAllSpec(
            @And({
                    @Spec(path = "model", spec = Equal.class)
            }) Specification<Measurement> measurementSpecification, Pageable pageable ){
        Page<Measurement> measurementPage = measurementService.getAllSpec(measurementSpecification,pageable);
        Page<MeasurementDto> measurementDtoPage = measurementPage.map(measurement -> conversionService.convert(measurement,MeasurementDto.class));
        return EntityResponse.listResponse(measurementDtoPage);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<MeasurementDto>> getAllSorted(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                             @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                             @RequestParam String field1, @RequestParam String field2) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,field1));
        orders.add(new Sort.Order(Sort.Direction.DESC,field2));
        Page<Measurement> measurementPage = measurementService.getAllSort(page,size,orders);
        Page<MeasurementDto> measurementDtoPage = measurementPage.map(measurement -> conversionService.convert(measurement,MeasurementDto.class));
        return EntityResponse.listResponse(measurementDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MeasurementDto> getMeasurementById(@PathVariable Integer id) throws MeasurementNotExistsException {
        MeasurementDto measurementDto = conversionService.convert(measurementService.getMeasurementById(id),MeasurementDto.class);
        return ResponseEntity.ok(measurementDto);
    }

    @PutMapping("/{id}/meters/{idMeter}")
    public ResponseEntity<Response> addMeterToMeasurement(@PathVariable Integer id, @PathVariable Integer idMeter) throws MeasurementNotExistsException, MeterNotExistsException {
        measurementService.addMeterToMeasurement(id, idMeter);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(EntityResponse.messageResponse("The Measurement has been modified"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMeasurementById(@PathVariable Integer id) throws MeasurementNotExistsException, RestrictDeleteException {
        measurementService.deleteMeasurementById(id);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();
    }

}
