package edu.utn.TPFinal.service;

import edu.utn.TPFinal.model.Brand;
import edu.utn.TPFinal.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class BrandService {

    private BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> getAll() {
        return brandRepository.findAll();
    }

    public Brand getByID(Integer id) {
        return brandRepository.findById(id).orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND,String.format("The Brand with ID: %d",id,"do not exists")));
    }

    public Brand addBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    public void deleteBrand(Brand brand) {
        brandRepository.delete(brand);
    }

    public void deleteBrandByID(Integer id) {
        brandRepository.deleteById(id);
    }
    
}
