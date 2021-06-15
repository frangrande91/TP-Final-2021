package edu.utn.TPFinal.controller.backoffice;

import edu.utn.TPFinal.exception.notFound.MeasurementNotExistsException;
import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.dto.MeasurementDto;
import edu.utn.TPFinal.service.MeasurementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static edu.utn.TPFinal.utils.MeasurementTestUtils.*;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeterPage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

public class MeasurementBackControllerTest {

    private static MeasurementService measurementService;
    private static ConversionService conversionService;
    private static MeasurementBackController measurementBackController;

    @BeforeEach
    public void setUp() {
        measurementService = mock(MeasurementService.class);
        conversionService = mock(ConversionService.class);
        measurementBackController = new MeasurementBackController(measurementService,conversionService);
    }

    @Test
    public void getAllMeters() {

        Page<Measurement> measurementPage = aMeasurementPage();
        Mockito.when(measurementService.getAllMeasurements(any())).thenReturn(aMeasurementPage());
        ResponseEntity<List<MeasurementDto>> responseEntity = measurementBackController.getAllMeasurements(1,1);

        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(measurementPage.getContent().size(),responseEntity.getBody().size());
        assertEquals(measurementPage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(measurementPage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }

    @Test
    public void getAllSpec() throws Exception {

        Specification<Measurement> measurementSpecification = Mockito.mock(Specification.class);

        Page<Measurement> measurementPage  = aMeasurementPage();
        Pageable pageable = PageRequest.of(1,1);

        Mockito.when(measurementService.getAllSpec(measurementSpecification,pageable)).thenReturn(measurementPage);
        ResponseEntity<List<MeasurementDto>> responseEntity = measurementBackController.getAllSpec(measurementSpecification,pageable);

        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(measurementPage.getContent().size(),responseEntity.getBody().size());
        assertEquals(measurementPage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(measurementPage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }

    @Test
    public void getAllSorted() {

        Page<Meter> meterPage = aMeterPage();
        Pageable pageable = PageRequest.of(1,1);

        Mockito.when(measurementService.getAllSort(any(),any(),anyList())).thenReturn(aMeasurementPage());
        ResponseEntity<List<MeasurementDto>> responseEntity = measurementBackController.getAllSorted(pageable.getPageNumber(),pageable.getPageSize(),"id","expiration");

        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(meterPage.getContent().size(),responseEntity.getBody().size());
        assertEquals(meterPage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(meterPage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }

    @Test
    public void getMeterById() {

        try {
            Mockito.when(measurementService.getMeasurementById(anyInt())).thenReturn(aMeasurement());
            Mockito.when(conversionService.convert(aMeasurement(),MeasurementDto.class)).thenReturn(aMeasurementDto());
            ResponseEntity<MeasurementDto> responseEntity = measurementBackController.getMeasurementById(1);

            assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
            assertEquals(aMeasurement().getId(), responseEntity.getBody().getId());
            assertEquals(aMeasurement().getDate(), responseEntity.getBody().getDate());
            assertEquals(aMeasurement().getPriceMeasurement(), responseEntity.getBody().getPriceMeasurement());
            assertEquals(aMeasurement().getQuantityKw(), responseEntity.getBody().getQuantityKw());
        } catch (MeasurementNotExistsException e) {
            fail(e);
        }
    }

}
