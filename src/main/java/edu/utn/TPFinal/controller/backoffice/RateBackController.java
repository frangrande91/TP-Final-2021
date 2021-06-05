package edu.utn.TPFinal.controller.backoffice;

import edu.utn.TPFinal.exceptions.ViolationChangeKeyAttributeException;
import edu.utn.TPFinal.exceptions.alreadyExists.RateAlreadyExists;
import edu.utn.TPFinal.exceptions.notFound.RateNotExistsException;
import edu.utn.TPFinal.model.dto.RateDto;
import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.model.responses.Response;
import edu.utn.TPFinal.service.RateService;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/backoffice/rates")
public class RateBackController {

    private RateService rateService;
    private ConversionService conversionService;
    private static final String RATE_PATH = "rates";

    @Autowired
    public RateBackController(RateService rateService, ConversionService conversionService) {
        this.rateService = rateService;
        this.conversionService = conversionService;
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @PostMapping(value = "/")
    public ResponseEntity<Response> addRate(@RequestBody Rate rate) throws RateAlreadyExists {
        Rate rateCreated = rateService.addRate(rate);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(EntityURLBuilder.buildURL2(RATE_PATH, rateCreated.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(EntityResponse.messageResponse("The rate has been created"));
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response> updateRate(@PathVariable Integer id,@RequestBody Rate rate) throws ViolationChangeKeyAttributeException, RateNotExistsException {
        rateService.updateRate(id,rate);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(EntityResponse.messageResponse("The rate has been updated"));
    }


    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<RateDto>> getAllRates(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                  @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Rate> ratePage = rateService.getAllRates(pageable);
        Page<RateDto> rateDtoPage = ratePage.map(rate -> conversionService.convert(rate, RateDto.class));
        return EntityResponse.listResponse(rateDtoPage);
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping("/spec")
    public ResponseEntity<List<RateDto>> getAllSpec(
            @And({
                    @Spec(path = "typeRate", spec = Equal.class)
            }) Specification<Rate> newsSpecification, Pageable pageable ){
        Page<Rate> ratePage = rateService.getAllSpec(newsSpecification,pageable);
        Page<RateDto> rateDtoPage = ratePage.map(rate -> conversionService.convert(rate, RateDto.class));
        return EntityResponse.listResponse(rateDtoPage);
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping("/sort")
    public ResponseEntity<List<RateDto>> getAllSorted(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                         @RequestParam String field1, @RequestParam String field2) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,field1));
        orders.add(new Sort.Order(Sort.Direction.DESC,field2));
        Page<Rate> ratePage = rateService.getAllSort(page, size, orders);
        Page<RateDto> rateDtoPage = ratePage.map(rate -> conversionService.convert(rate, RateDto.class));
        return EntityResponse.listResponse(rateDtoPage);
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<RateDto> getRateById(@PathVariable Integer id) throws RateNotExistsException {
        Rate rate = rateService.getRateById(id);
        RateDto rateDto = conversionService.convert(rate,RateDto.class);
        return ResponseEntity.ok(rateDto);
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteRateById(@PathVariable Integer id) throws RateNotExistsException{
        rateService.deleteRateById(id);
        return ResponseEntity.accepted().build();
    }

}
