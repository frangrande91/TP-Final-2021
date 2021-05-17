package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.model.Model;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.service.ModelService;
import edu.utn.TPFinal.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping(value = "/")
    public PostResponse addModel(@RequestBody Model model) {
        return modelService.addModel(model);
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<Model>> getAllModels() {
        List<Model> models = modelService.getAllModels();
        return response(models);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Model> getModelByID(@PathVariable Integer id) {
        Model model = modelService.getModelByID(id);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping(value = "/")
    public void deleteModel(@RequestBody Model model) {
        modelService.deleteModel(model);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteModelById(@PathVariable Integer id) {
        modelService.deleteModelById(id);
    }

    private ResponseEntity response(List list){
        return ResponseEntity.status(list.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK).body(list);
    }

}
