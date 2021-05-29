package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.exceptions.notFound.ModelNotExistsException;
import edu.utn.TPFinal.model.dto.ModelDto;
import edu.utn.TPFinal.model.Model;
import edu.utn.TPFinal.model.responses.Response;
import edu.utn.TPFinal.service.ModelService;
import edu.utn.TPFinal.utils.EntityResponse;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/model")
public class ModelController {

    private ModelService modelService;
    private ConversionService conversionService;
    private static final String MODEL_PATH = "model";

    @Autowired
    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }


    @PostMapping(value = "/")
    public ResponseEntity<Response> addModel(@RequestBody Model model) throws SQLIntegrityConstraintViolationException {
        Model modelCreated = modelService.addModel(model);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(EntityURLBuilder.buildURL2(MODEL_PATH, modelCreated.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(EntityResponse.messageResponse("The model has been created"));
    }

    @GetMapping
    public ResponseEntity<List<ModelDto>> getAllModels(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                       @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Model> modelPage = modelService.getAllModels(pageable);
        Page<ModelDto> modelDtoPage = modelPage.map(model -> conversionService.convert(model, ModelDto.class));
        return EntityResponse.listResponse(modelDtoPage);
    }

    @GetMapping("/spec")
    public ResponseEntity<List<ModelDto>> getAllSpec(
            @And({
                    @Spec(path = "brand", spec = Equal.class),
                    @Spec(path = "name", spec = Equal.class)
            }) Specification<Model> newsSpecification, Pageable pageable ){
        Page<Model> modelPage = modelService.getAllSpec(newsSpecification,pageable);
        Page<ModelDto> modelDtoPage = modelPage.map(model -> conversionService.convert(model, ModelDto.class));
        return EntityResponse.listResponse(modelDtoPage);
    }


    @GetMapping("/sort")
    public ResponseEntity<List<ModelDto>> getAllSorted(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                         @RequestParam String field1, @RequestParam String field2) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,field1));
        orders.add(new Sort.Order(Sort.Direction.DESC,field2));
        Page<Model> modelPage = modelService.getAllSort(page,size,orders);
        Page<ModelDto> modelDtoPage = modelPage.map(model -> conversionService.convert(model, ModelDto.class));
        return EntityResponse.listResponse(modelDtoPage);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Model> getModelByID(@PathVariable Integer id) throws ModelNotExistsException {
        Model model = modelService.getModelById(id);
        return ResponseEntity.ok(model);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteModelById(@PathVariable Integer id) throws ModelNotExistsException{
        modelService.deleteModelById(id);
        return ResponseEntity.accepted().build();
    }

}
