package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.BrandNotExistsException;
import edu.utn.TPFinal.model.Brand;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.repository.BrandRepository;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.SQLIntegrityConstraintViolationException;

import static java.util.Objects.isNull;

@Service
public class BrandService {

    private static final String BRAND_PATH = "brand";
    private BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Brand addBrand(Brand brand) throws SQLIntegrityConstraintViolationException {
            return brandRepository.save(brand);
    }

    public Page<Brand> getAllBrands(Pageable pageable) {
        return brandRepository.findAll(pageable);
    }


    public Brand getBrandByID(Integer id) throws BrandNotExistsException {
        return brandRepository.findById(id).orElseThrow(BrandNotExistsException::new);
    }

    public void deleteBrand(Brand brand) {
        brandRepository.delete(brand);
    }

    public void deleteBrandByID(Integer id) {
        brandRepository.deleteById(id);
    }
    
}
