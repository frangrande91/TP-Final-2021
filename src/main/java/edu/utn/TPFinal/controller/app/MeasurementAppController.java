package edu.utn.TPFinal.controller.app;

import edu.utn.TPFinal.exception.AccessNotAllowedException;
import edu.utn.TPFinal.exception.notFound.MeterNotExistsException;
import edu.utn.TPFinal.exception.notFound.UserNotExistsException;
import edu.utn.TPFinal.model.dto.MeasurementDto;
import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.model.dto.UserDto;
import edu.utn.TPFinal.model.response.ClientConsumption;
import edu.utn.TPFinal.service.MeasurementService;
import edu.utn.TPFinal.utils.EntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static edu.utn.TPFinal.utils.Utils.checkFromTo;

@RestController
@RequestMapping("/app/measurements")
public class MeasurementAppController {

    private MeasurementService measurementService;
    private ConversionService conversionService;

    @Autowired
    public MeasurementAppController(MeasurementService measurementService, ConversionService conversionService) {
        this.measurementService = measurementService;
        this.conversionService = conversionService;
    }

    /**Punto 4*/
    @PreAuthorize(value = "hasAuthority('EMPLOYEE') OR hasAuthority('CLIENT')")
    @GetMapping("/meters/{idMeter}/consumption")
    public ResponseEntity<ClientConsumption> getConsumptionByMeter(@PathVariable Integer idMeter,
                                                              @RequestParam(value = "from", defaultValue = "2020-12-05") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                                              @RequestParam(value = "to", defaultValue = "2020-01-05") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate to,
                                                              Authentication authentication) throws UserNotExistsException, AccessNotAllowedException, MeterNotExistsException {
        checkFromTo(from,to);
        UserDto userDto = (UserDto) authentication.getPrincipal();
        ClientConsumption clientConsumption = measurementService.getConsumptionByMeterAndDateBetween(idMeter, userDto.getId(), from, to);
        return ResponseEntity.status(HttpStatus.OK).body(clientConsumption);
    }

    /**Punto 5*/
    @PreAuthorize(value = "hasAuthority('EMPLOYEE') OR hasAuthority('CLIENT')")
    @GetMapping("/meters/{idMeter}")
    public ResponseEntity<List<MeasurementDto>> getAllByMeter(@PathVariable Integer idMeter,
                                                                       @RequestParam(value = "from", defaultValue = "2021-01-05") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                                                       @RequestParam(value = "to", defaultValue = "2021-12-05") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate to,
                                                                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                       Authentication authentication) throws UserNotExistsException, AccessNotAllowedException, MeterNotExistsException {
        checkFromTo(from,to);
        UserDto userDto = (UserDto) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page, size);
        Page<Measurement> measurementPage = measurementService.getAllByMeterAndDateBetween(idMeter,userDto.getId(),from,to,pageable);
        Page<MeasurementDto> measurementDtoPage = measurementPage.map(measurement -> conversionService.convert(measurement,MeasurementDto.class));
        return EntityResponse.listResponse(measurementDtoPage);
    }

}
