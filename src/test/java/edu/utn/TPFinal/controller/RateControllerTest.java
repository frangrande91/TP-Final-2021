package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.AbstractController;
import edu.utn.TPFinal.service.RateService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static edu.utn.TPFinal.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RateController.class)
@Slf4j
public class RateControllerTest extends AbstractController {

    @MockBean
    private RateService rateService;
    @MockBean
    private FormattingConversionService formattingConversionService;

    @Test
    public void getAllRates() throws Exception {

        Mockito.when(rateService.getAllRates(any())).thenReturn(aRatePage());

        final ResultActions resultActions = givenController()
                .perform(MockMvcRequestBuilders
                .get("/rates/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(HttpStatus.OK.value(),resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void getAllSpec() throws Exception {

    }

    @Test
    public void getAllSorted()  throws Exception{
        Mockito.when(rateService.getAllSort(any(),any(),anyList())).thenReturn(aRatePage());

        final ResultActions resultActions = givenController()
                .perform(MockMvcRequestBuilders
                .get("/rates/sort?field1=id&field2=value")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(HttpStatus.OK.value(),resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void getRateById() throws Exception {

        final ResultActions resultActions = givenController()
                .perform(MockMvcRequestBuilders
                        .get("/rates/1")
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

        assertEquals(HttpStatus.OK.value(),resultActions.andReturn().getResponse().getStatus());

    }

    @Test
    public void addRate() throws Exception {

        Mockito.when(rateService.addRate(aRate())).thenReturn(aRate());

        ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                        .post("/rates/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(aRateJSON()))
                .andExpect(status().isCreated());

        assertEquals(HttpStatus.CREATED.value(),resultActions.andReturn().getResponse().getStatus());

    }

    @Test
    public void deleteRateById() throws Exception {

        ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                    .delete("/rates/1")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isAccepted());

        assertEquals(HttpStatus.ACCEPTED.value(),resultActions.andReturn().getResponse().getStatus());
    }

}
