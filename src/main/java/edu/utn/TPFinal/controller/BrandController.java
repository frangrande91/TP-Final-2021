package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.exceptions.BrandNotExistsException;
import edu.utn.TPFinal.model.Brand;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
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

    @GetMapping(value = "/")
    public ResponseEntity<List<Brand>> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        return response(brands);
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


    private ResponseEntity response(List list){
        return ResponseEntity.status(list.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK).body(list);
    }

}
