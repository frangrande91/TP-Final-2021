package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exception.notFound.AddressNotExistsException;
import edu.utn.TPFinal.exception.notFound.ModelNotExistsException;
import edu.utn.TPFinal.model.Model;
import edu.utn.TPFinal.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModelService {

    private ModelRepository modelRepository;


    @Autowired
    public ModelService(ModelRepository modelRepository){
        this.modelRepository = modelRepository;
    }

    public Model getModelById(Integer id) throws ModelNotExistsException {
        return modelRepository.findById(id).orElseThrow(() -> new ModelNotExistsException("Model not exists"));
    }
}
