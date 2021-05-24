package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.exceptions.BrandNotExistsException;
import edu.utn.TPFinal.model.Brand;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/brand")
@Slf4j
public class BrandController {

    private BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping(value = "/")
    public PostResponse addBrand(@RequestBody Brand brand) {
        return brandService.addBrand(brand);
    }

    @GetMapping
    public ResponseEntity getAllBrands(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Brand> brandPage = brandService.getAllBrands(pageable);
        return response(brandPage);
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

    /*
    private ResponseEntity response(List list){
        return ResponseEntity.status(list.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK).body(list);
    }

    */

    private ResponseEntity response(Page<Brand> pageBrand){
        if(!pageBrand.getContent().isEmpty()){
           return ResponseEntity.
                    status(HttpStatus.OK).
                    header("X-Total-Count", Long.toString(pageBrand.getTotalElements())).
                    header("X-Total-Pages", Long.toString(pageBrand.getTotalPages())).
                    body(pageBrand.getContent());
        }
        else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(pageBrand.getContent());
        }

    }
}
