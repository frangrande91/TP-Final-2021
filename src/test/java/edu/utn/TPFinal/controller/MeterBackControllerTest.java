package edu.utn.TPFinal.controller;
import edu.utn.TPFinal.controller.backoffice.MeterController;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.dto.MeterDto;
import edu.utn.TPFinal.model.responses.Response;
import edu.utn.TPFinal.service.MeterService;
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

import static edu.utn.TPFinal.utils.MeterTestUtils.*;
import static edu.utn.TPFinal.utils.RateTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import java.util.*;


public class MeterControllerTest {


    private static MeterService meterService;
    private static ConversionService conversionService;
    private static MeterController meterController;

    @BeforeAll
    public static void setUp() {
        meterService = Mockito.mock(MeterService.class);
        conversionService = Mockito.mock(ConversionService.class);
        meterController = new MeterController(meterService,conversionService);
    }


    @Test
    public void getAllMeters() throws Exception {

        Page<Meter> meterPage = aMeterPage();
        Mockito.when(meterService.getAllMeters(any())).thenReturn(meterPage);
        ResponseEntity<List<MeterDto>> responseEntity = meterController.getAllMeters(1,1);

        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(meterPage.getContent().size(),responseEntity.getBody().size());
        assertEquals(meterPage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(meterPage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }

    @Test
    public void getAllSpec() throws Exception {

        Specification<Meter> meterSpecification = Mockito.mock(Specification.class);

        Page<Meter> meterPage  = aMeterPage();
        Pageable pageable = PageRequest.of(1,1);

        Mockito.when(meterService.getAllSpec(meterSpecification,pageable)).thenReturn(meterPage);
        ResponseEntity<List<MeterDto>> responseEntity = meterController.getAllSpec(meterSpecification,pageable);


        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(meterPage.getContent().size(),responseEntity.getBody().size());
        assertEquals(meterPage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(meterPage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }

    @Test
    public void getAllSorted() throws Exception {

        Page<Meter> meterPage = aMeterPage();
        Pageable pageable = PageRequest.of(1,1);

        Mockito.when(meterService.getAllSort(any(),any(),anyList())).thenReturn(aMeterPage());
        ResponseEntity<List<MeterDto>> responseEntity = meterController.getAllSorted(pageable.getPageNumber(),pageable.getPageSize(),"id","serialNumber");

        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(meterPage.getContent().size(),responseEntity.getBody().size());
        assertEquals(meterPage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(meterPage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }

    @Test
    public void getMeterById() throws Exception {

        Mockito.when(meterService.getMeterById(anyInt())).thenReturn(aMeter());
        Mockito.when(conversionService.convert(aMeter(),MeterDto.class)).thenReturn(aMeterDto());
        ResponseEntity<MeterDto> responseEntity = meterController.getMeterById(1);

        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(aMeterDto().getId(), responseEntity.getBody().getId());
        assertEquals(aMeterDto().getModel(), responseEntity.getBody().getModel());
        assertEquals(aMeterDto().getPassword(), responseEntity.getBody().getPassword());
        assertEquals(aMeterDto().getSerialNumber(), responseEntity.getBody().getSerialNumber());

    }

    @Test
    public void addMeter() throws Exception{

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Mockito.when(meterService.addMeter(aMeter())).thenReturn(aMeter());
        ResponseEntity<Response> responseEntity = meterController.addMeter(aMeter());

        assertEquals(EntityURLBuilder.buildURL2("meters", aRate().getId()).toString(),responseEntity.getHeaders().get("Location").get(0));
        assertEquals(HttpStatus.CREATED.value(),responseEntity.getStatusCode().value());
    }

    @Test
    public void deleteMeterById() throws Exception{
        Mockito.doNothing().when(meterService).deleteMeterById(1);
        ResponseEntity<Object> responseEntity = meterController.deleteMeterById(1);
        assertEquals(HttpStatus.ACCEPTED.value(),responseEntity.getStatusCode().value());
    }

}
