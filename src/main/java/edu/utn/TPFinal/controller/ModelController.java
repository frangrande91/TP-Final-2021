package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.model.Model;
import edu.utn.TPFinal.service.ModelService;
import edu.utn.TPFinal.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@RestController
@RequestMapping(value = "/model")
public class ModelController {

    private ModelService modelService;

    @Autowired
    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping(value = "/")
    public List<Model> getAll() {
        return modelService.getAll();
    }

    @GetMapping(value = "/{id}")
    public Model getByID(@PathVariable Integer id) {
        return modelService.getByID(id);
    }

    @PostMapping(value = "/")
    public Model addRate(@RequestBody Model model) {
        return modelService.addRate(model);
    }

    @DeleteMapping(value = "/")
    public void deleteModel(@RequestBody Model model) {
        modelService.deleteModel(model);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteModelById(@PathVariable Integer id) {
        modelService.deleteModelById(id);
    }

}
