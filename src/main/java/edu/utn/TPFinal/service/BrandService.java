package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.notFound.BrandNotExistsException;
import edu.utn.TPFinal.model.Brand;
import edu.utn.TPFinal.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

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

    public Page<Brand> getAllSpec(Specification<Brand> brandSpecification, Pageable pageable) {
        return brandRepository.findAll(brandSpecification, pageable);
    }

    public Page<Brand> getAllSort(Integer page, Integer size, List<Sort.Order> orders) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(orders));
        return brandRepository.findAll(pageable);
    }

    public Brand getBrandById(Integer id) throws BrandNotExistsException {
        return brandRepository.findById(id).orElseThrow(() -> new BrandNotExistsException("Brand not exists"));
    }

    public void deleteBrandByID(Integer id) throws BrandNotExistsException{
        getBrandById(id);
        brandRepository.deleteById(id);
    }

}
