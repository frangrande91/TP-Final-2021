package edu.utn.TPFinal.service;

import edu.utn.TPFinal.model.Model;
import edu.utn.TPFinal.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;


@Service
public class ModelService {

    private ModelRepository modelRepository;

    @Autowired
    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public Model addModel(Model model) throws SQLIntegrityConstraintViolationException{
        return modelRepository.save(model);
    }

    public Page<Model> getAllModels(Pageable pageable) {
        return modelRepository.findAll(pageable);
    }

    public Page<Model> getAllSort(Integer page, Integer size, List<Sort.Order> orders) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(orders));
        return modelRepository.findAll(pageable);
    }

    public Page<Model> getAllSpec(Specification<Model> modelSpecification, Pageable pageable) {
        return modelRepository.findAll(modelSpecification,pageable);
    }

    public Model getModelById(Integer id) {
        return modelRepository.findById(id).orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND,String.format("The model with ID: %d",id,"do not exists")));
    }

    /*
    public void deleteModel(Model model) {
        modelRepository.delete(model);
    }

    public void deleteModelById(Integer id) {
        modelRepository.deleteById(id);
    }
     */

}
