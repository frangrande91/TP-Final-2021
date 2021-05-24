package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.exceptions.BrandNotExistsException;
import edu.utn.TPFinal.model.Brand;
import edu.utn.TPFinal.model.Responses.Response;
import edu.utn.TPFinal.service.BrandService;
import edu.utn.TPFinal.utils.EntityResponse;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequestMapping("/brand")
@Slf4j
public class BrandController {

    private BrandService brandService;
    private final String BRAND_PATH = "brand";

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping(value = "/")
    public ResponseEntity addBrand(@RequestBody Brand brand) throws SQLIntegrityConstraintViolationException {
        Brand brandCreated = brandService.addBrand(brand);

        return ResponseEntity
                .status(HttpStatus.OK)
                .location(EntityURLBuilder.buildURL2(BRAND_PATH,brandCreated.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Response.builder().message("The brand has been created").build());
    }

    @GetMapping
    public ResponseEntity getAllBrands(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Brand> brandPage = brandService.getAllBrands(pageable);
        return EntityResponse.response(brandPage);

    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Brand> getBrandByID(@PathVariable Integer id) throws BrandNotExistsException {
        Brand brand = brandService.getBrandByID(id);
        return ResponseEntity.ok(brand);
    }

    @DeleteMapping(value = "/")
    public void deleteBrand(@RequestBody Brand brand) {
        brandService.deleteBrand(brand);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteBrandID(@PathVariable Integer id) {
        brandService.deleteBrandByID(id);
    }

}
