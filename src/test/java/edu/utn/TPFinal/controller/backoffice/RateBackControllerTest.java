package edu.utn.TPFinal.controller.backoffice;

import edu.utn.TPFinal.controller.backoffice.RateBackController;
import edu.utn.TPFinal.exception.ViolationChangeKeyAttributeException;
import edu.utn.TPFinal.exception.notFound.RateNotExistsException;
import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.model.dto.RateDto;
import edu.utn.TPFinal.model.response.Response;
import edu.utn.TPFinal.service.RateService;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static edu.utn.TPFinal.utils.EntityResponse.messageResponse;
import static edu.utn.TPFinal.utils.RateTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import java.util.*;


public class RateBackControllerTest {

    private static RateService rateService;
    private static ConversionService conversionService;
    private static RateBackController rateBackController;

    @BeforeAll
    public static void setUp() {
        rateService = Mockito.mock(RateService.class);
        conversionService = Mockito.mock(ConversionService.class);
        rateBackController = new RateBackController(rateService,conversionService);
    }


    @Test
    public void getAllRates() throws Exception {

        Page<Rate> ratePage = aRatePage();

        Mockito.when(rateService.getAllRates(any())).thenReturn(ratePage);
        ResponseEntity<List<RateDto>> responseEntity = rateBackController.getAllRates(1,1);


        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(ratePage.getContent().size(),responseEntity.getBody().size());
        assertEquals(ratePage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(ratePage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));

    }

    @Test
    public void getAllSpec() {

        Specification<Rate> rateSpecification = Mockito.mock(Specification.class);

        Page<Rate> ratePage = aRatePage();
        Pageable pageable = PageRequest.of(1,1);

        Mockito.when(rateService.getAllSpec(rateSpecification,pageable)).thenReturn(ratePage);
        ResponseEntity<List<RateDto>> responseEntity = rateBackController.getAllSpec(rateSpecification,pageable);


        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(ratePage.getContent().size(),responseEntity.getBody().size());
        assertEquals(ratePage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(ratePage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }

    @Test
    public void getAllSorted() {
        Page<Rate> ratePage = aRatePage();
        Pageable pageable = PageRequest.of(1,1);

        Mockito.when(rateService.getAllSort(any(),any(),anyList())).thenReturn(aRatePage());
        ResponseEntity<List<RateDto>> responseEntity = rateBackController.getAllSorted(pageable.getPageNumber(),pageable.getPageSize(),"id","typeRate");


        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(ratePage.getContent().size(),responseEntity.getBody().size());
        assertEquals(ratePage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(ratePage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }

    @Test
    public void getRateById() throws Exception {

        Mockito.when(rateService.getRateById(anyInt())).thenReturn(aRate());
        Mockito.when(conversionService.convert(aRate(),RateDto.class)).thenReturn(aRateDto());
        ResponseEntity<RateDto> responseEntity = rateBackController.getRateById(1);

        assertEquals(aRate().getId(),responseEntity.getBody().getId());
        assertEquals(aRate().getTypeRate(),responseEntity.getBody().getTypeRate());
        assertEquals(aRate().getValue(),responseEntity.getBody().getValue());
        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());

    }

    @Test
    public void addRate() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Mockito.when(rateService.addRate(aRate())).thenReturn(aRate());
        ResponseEntity<Response> responseEntity = rateBackController.addRate(aRate());

        assertEquals(EntityURLBuilder.buildURL2("rates", aRate().getId()).toString(),responseEntity.getHeaders().get("Location").get(0));
        assertEquals(HttpStatus.CREATED.value(),responseEntity.getStatusCode().value());
    }

    @Test
    public void deleteRateById() throws Exception {
        Mockito.doNothing().when(rateService).deleteRateById(1);
        ResponseEntity<Object> responseEntity = rateBackController.deleteRateById(1);
        assertEquals(HttpStatus.ACCEPTED.value(),responseEntity.getStatusCode().value());
    }

    @Test
    public void updateRate() {
        try {
            Mockito.when(rateService.updateRate(any(),any())).thenReturn(aRate());

            ResponseEntity<Response> responseEntity = rateBackController.updateRate(1,aRate());

            assertEquals(HttpStatus.ACCEPTED.value(),responseEntity.getStatusCode().value());
            assertEquals(messageResponse("The rate has been updated"),responseEntity.getBody());

        }
        catch ( ViolationChangeKeyAttributeException | RateNotExistsException e) {
            fail(e);
        }

    }

}
