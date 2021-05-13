package edu.utn.TPFinal.service;
import edu.utn.TPFinal.model.Model;
import edu.utn.TPFinal.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class ModelService {

    private ModelRepository modelRepository;

    @Autowired
    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public List<Model> getAll() {
        return modelRepository.findAll();
    }

    public Model getByID(Integer id) {
        return modelRepository.findById(id).orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND,String.format("The model with ID: %d",id,"do not exists")));
    }

    public Model addRate(Model model) {
        return modelRepository.save(model);
    }

    public void deleteModel(Model model) {
        modelRepository.delete(model);
    }

    public void deleteModelById(Integer id) {
        modelRepository.deleteById(id);
    }

}
