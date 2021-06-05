package edu.utn.TPFinal.controller.backoffice;

import edu.utn.TPFinal.model.dto.UserDto;
import edu.utn.TPFinal.service.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/backoffice/measurements")
public class MeasurementsBackController {

    private MeasurementService measurementService;
    private ConversionService conversionService;

    @Autowired
    public MeasurementsBackController(MeasurementService measurementService,ConversionService conversionService) {
        this.measurementService = measurementService;
        this.conversionService = conversionService;
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping("{from}/{to}")
    public ResponseEntity<List<UserDto>> get10MoreConsumersPerDateRange(@PathVariable LocalDateTime from, @PathVariable LocalDateTime to) {
        return null;
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping("addresses/{idAddress}/{from}/{to}")
    public ResponseEntity<List<UserDto>> getByAddressForDateRange(@PathVariable LocalDateTime idAddress, @PathVariable LocalDateTime from, @PathVariable LocalDateTime to) {
        return null;
    }


}
