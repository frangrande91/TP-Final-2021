package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exception.notFound.ModelNotExistsException;
import edu.utn.TPFinal.model.Model;
import edu.utn.TPFinal.repository.ModelRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static edu.utn.TPFinal.utils.ModelTestUtils.aModel;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ModelServiceTest {

    private static ModelRepository modelRepository;
    private static ModelService modelService;

    @BeforeAll
    public static void setUp(){
        modelRepository = mock(ModelRepository.class);
        modelService = new ModelService(modelRepository);
    }


    @Test
    public void getModelByIdOk(){
        when(modelRepository.findById(1)).thenReturn(Optional.of(aModel()));

        try {
            Model model = modelService.getModelById(1);

            assertEquals(aModel(), model);
            verify(modelRepository, times(1)).findById(1);
        } catch (ModelNotExistsException e) {
            fail(e);
        }
    }

    @Test
    public void getModelByIdNotExists(){
        when(modelRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ModelNotExistsException.class, () -> modelService.getModelById(1));
    }

}
