package edu.utn.TPFinal.service;
import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.model.Model;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.repository.ModelRepository;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class ModelService {

    private static final String MODEL_PATH = "model";
    private ModelRepository modelRepository;

    @Autowired
    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }

    public PostResponse addModel(Model model) {
        Model m = modelRepository.save(model);
        return PostResponse
                .builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(MODEL_PATH, m.getId()))
                .build();
    }

    public List<Model> getAllModels() {
        return modelRepository.findAll();
    }

    public Model getModelByID(Integer id) {
        return modelRepository.findById(id).orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND,String.format("The model with ID: %d",id,"do not exists")));
    }

    public void deleteModel(Model model) {
        modelRepository.delete(model);
    }

    public void deleteModelById(Integer id) {
        modelRepository.deleteById(id);
    }

}
