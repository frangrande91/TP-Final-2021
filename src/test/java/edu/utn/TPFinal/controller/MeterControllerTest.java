package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.AbstractController;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.dto.MeterDto;
import edu.utn.TPFinal.service.MeterService;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import lombok.extern.slf4j.Slf4j;
import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;
import org.h2.command.dml.MergeUsing;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.HandlerResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static edu.utn.TPFinal.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = MeterController.class)
@Slf4j
public class MeterControllerTest extends AbstractController {


    @MockBean
    private MeterService meterService;
    @MockBean
    private FormattingConversionService formattingConversionService;
    @Test
    public void getAllMeters() throws Exception {

        Mockito.when(meterService.getAllMeters(any(Pageable.class))).thenReturn(aMeterPage());

        final ResultActions resultActions = givenController()
                .perform(MockMvcRequestBuilders
                .get("/meter/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void getAllSpec() throws Exception {

        Mockito.when(meterService.getAllSpec(specMeter("123456"), PageRequest.of(1,1))).thenReturn(aMeterPage());

        final ResultActions resultActions = givenController()
                .perform(MockMvcRequestBuilders
                        .get("/meter/spec?serialNumber=123456")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                ;

        assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void getAllSorted() throws Exception {

        Mockito.when(meterService.getAllSort(any(),any(),anyList())).thenReturn(aMeterPage());

        final ResultActions resultActions = givenController()
                .perform(MockMvcRequestBuilders
                        .get("/meter/sort?field1=serialNumber&field2=id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                ;

        assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void getMeterById() throws Exception {
        final ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                .get("/meter/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(HttpStatus.OK.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void addMeter() throws Exception{

        Mockito.when(meterService.addMeter(aMeter())).thenReturn(aMeter());

        ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                .post("/meter/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aMeterJSON()))
                .andExpect(status().isCreated())
                ;

        assertEquals(HttpStatus.CREATED.value(), resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void deleteMeterById() throws Exception{

        ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                .delete("/meter/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        assertEquals(HttpStatus.ACCEPTED.value(), resultActions.andReturn().getResponse().getStatus());
    }

}
