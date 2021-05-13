package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.model.Brand;
import edu.utn.TPFinal.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(value = "/")
    public List<Brand> getAll() {
        return brandService.getAll();
    }

    @GetMapping(value = "/{id}")
    public Brand getByID(@PathVariable Integer id) {
        return brandService.getByID(id);
    }

    @PostMapping(value = "/")
    public Brand addBrand(@RequestBody Brand brand) {
        return brandService.addBrand(brand);
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
