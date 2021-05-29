package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.exceptions.notFound.MeterNotExistsException;
import edu.utn.TPFinal.model.dto.MeterDto;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.responses.Response;
import edu.utn.TPFinal.service.MeterService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/meter")
public class MeterController {

    private final MeterService meterService;
    private final ConversionService conversionService;
    private final String METER_PATH = "meter";

    @Autowired
    public MeterController(MeterService meterService,ConversionService conversionService) {
        this.meterService = meterService;
        this.conversionService = conversionService;
    }

    @PostMapping("/")
    public ResponseEntity<Response> addMeter(@RequestBody Meter meter){
        Meter meterCreated = meterService.addMeter(meter);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(EntityURLBuilder.buildURL2(METER_PATH,meterCreated.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(EntityResponse.messageResponse("The meter has been created"));
    }

    @GetMapping()
    public ResponseEntity<List<MeterDto>> getAllMeters(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Meter> meterPage = meterService.getAllMeters(pageable);
        Page<MeterDto> meterDtoPage = meterPage.map(meter -> conversionService.convert(meter,MeterDto.class));
        return EntityResponse.listResponse(meterDtoPage);
    }

    @GetMapping("/spec")
    public ResponseEntity<List<MeterDto>> getAllSpec(
            @And({
                    @Spec(path = "address", spec = Equal.class),
                    @Spec(path = "userClient", spec = Equal.class),
                    @Spec(path = "meter", spec = Equal.class)
            }) Specification<Meter> meterSpecifications, Pageable pageable ) {

        Page<Meter> meterPage = meterService.getAllSpec(meterSpecifications,pageable);
        Page<MeterDto> meterDtoPage = meterPage.map(meter -> conversionService.convert(meter,MeterDto.class));
        return EntityResponse.listResponse(meterDtoPage);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<MeterDto>> getAllSorted(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                       @RequestParam String field1, @RequestParam String field2) {

        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.DESC,field1));
        orders.add(new Order(Sort.Direction.DESC,field2));
        Page<Meter> meterPage = meterService.getAllSort(page,size,orders);
        Page<MeterDto> meterDtoPage = meterPage.map(meter -> conversionService.convert(meter,MeterDto.class));
        return EntityResponse.listResponse(meterDtoPage);

    }

    @GetMapping("/{id}")
    public ResponseEntity<MeterDto> getMeterById(@PathVariable Integer id) throws MeterNotExistsException {
        MeterDto meterDto = conversionService.convert(meterService.getMeterById(id), MeterDto.class);
        return ResponseEntity.ok(meterDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMeterById(@PathVariable Integer id) throws MeterNotExistsException{
        meterService.deleteMeterById(id);
        return ResponseEntity.accepted().build();
    }

}