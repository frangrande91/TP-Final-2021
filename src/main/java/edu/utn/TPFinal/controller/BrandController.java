package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.exceptions.notFound.BrandNotExistsException;
import edu.utn.TPFinal.model.Brand;
import edu.utn.TPFinal.model.dto.BrandDto;
import edu.utn.TPFinal.model.responses.Response;
import edu.utn.TPFinal.service.BrandService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/brand")
@Slf4j
public class BrandController {

    private BrandService brandService;
    private ConversionService conversionService;
    private static final String BRAND_PATH = "brand";

    @Autowired
    public BrandController(BrandService brandService, ConversionService conversionService) {
        this.brandService = brandService;
        this.conversionService = conversionService;
    }


    @PostMapping(value = "/")
    public ResponseEntity<Response> addBrand(@RequestBody Brand brand) throws SQLIntegrityConstraintViolationException {
        Brand brandCreated = brandService.addBrand(brand);

        return ResponseEntity
                .status(HttpStatus.OK)
                .location(EntityURLBuilder.buildURL2(BRAND_PATH,brandCreated.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(EntityResponse.messageResponse("The brand has been created"));
    }

    @GetMapping
    public ResponseEntity<List<BrandDto>> getAllBrands(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Brand> brandPage = brandService.getAllBrands(pageable);
        Page<BrandDto> brandDtoPage = brandPage.map(brand -> conversionService.convert(brand, BrandDto.class));
        return EntityResponse.listResponse(brandDtoPage);

    }

    @GetMapping("/sort")
    public ResponseEntity<List<BrandDto>> getAllSorted(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                         @RequestParam String field1, @RequestParam String field2) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,field1));
        orders.add(new Sort.Order(Sort.Direction.DESC,field2));
        Page<Brand> brandPage = brandService.getAllSort(page,size,orders);
        Page<BrandDto> brandDtoPage = brandPage.map(brand -> conversionService.convert(brand, BrandDto.class));
        return EntityResponse.listResponse(brandDtoPage);
    }

    @GetMapping("/spec")
    public ResponseEntity<List<BrandDto>> getAllSpec(
            @And({
                    @Spec(path = "name", spec = Equal.class)
            }) Specification<Brand> newsSpecification, Pageable pageable ){
        Page<Brand> brandPage = brandService.getAllSpec(newsSpecification,pageable);
        Page<BrandDto> brandDtoPage = brandPage.map(brand -> conversionService.convert(brand, BrandDto.class));
        return EntityResponse.listResponse(brandDtoPage);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Brand> getBrandByID(@PathVariable Integer id) throws BrandNotExistsException {
        Brand brand = brandService.getBrandById(id);
        return ResponseEntity.ok(brand);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteBrandById(@PathVariable Integer id) throws BrandNotExistsException {
        brandService.deleteBrandByID(id);
        return ResponseEntity.accepted().build();
    }

}
