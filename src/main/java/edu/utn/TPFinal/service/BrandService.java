package edu.utn.TPFinal.service;

import edu.utn.TPFinal.model.Brand;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.repository.BrandRepository;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class BrandService {

    private static final String BRAND_PATH = "brand";
    private BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public PostResponse addBrand(Brand brand) {
        Brand b = brandRepository.save(brand);
        return PostResponse
                .builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(BRAND_PATH, b.getId()))
                .build();
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Brand getBrandByID(Integer id) {
        return brandRepository.findById(id).orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND,String.format("The Brand with ID: %d",id,"do not exists")));
    }

    public void deleteBrand(Brand brand) {
        brandRepository.delete(brand);
    }

    public void deleteBrandByID(Integer id) {
        brandRepository.deleteById(id);
    }
    
}
