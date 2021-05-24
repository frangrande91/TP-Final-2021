package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.model.Model;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity getAllModels(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Model> modelPage = modelService.getAllModels(pageable);
        return response(modelPage);
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

    /*
    private ResponseEntity response(List list){
        return ResponseEntity.status(list.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK).body(list);
    }
     */

    private ResponseEntity response(Page<Model> pageBrand){
        if(!pageBrand.getContent().isEmpty()){
            return ResponseEntity.
                    status(HttpStatus.OK).
                    header("X-Total-Count", Long.toString(pageBrand.getTotalElements())).
                    header("X-Total-Pages", Long.toString(pageBrand.getTotalPages())).
                    body(pageBrand.getContent());
        }
        else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(pageBrand.getContent());
        }

    }

}
