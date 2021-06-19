package edu.utn.TPFinal.controller.backoffice;

import edu.utn.TPFinal.exception.RestrictDeleteException;
import edu.utn.TPFinal.exception.notFound.AddressNotExistsException;
import edu.utn.TPFinal.exception.notFound.MeasurementNotExistsException;
import edu.utn.TPFinal.exception.notFound.MeterNotExistsException;
import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.dto.MeasurementDto;
import edu.utn.TPFinal.model.response.Response;
import edu.utn.TPFinal.service.MeasurementService;
import edu.utn.TPFinal.utils.EntityURLBuilder;
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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import static edu.utn.TPFinal.utils.EntityResponse.messageResponse;
import static edu.utn.TPFinal.utils.MeasurementTestUtils.*;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeterPage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
    public void addMeasurement() {
        try {
            MockHttpServletRequest request = new MockHttpServletRequest();
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            when(measurementService.addMeasurement(aReceivedMeasurementDto())).thenReturn(aMeasurement());
            ResponseEntity<Response> responseEntity = measurementBackController.addMeasurement(aReceivedMeasurementDto());


            assertEquals(EntityURLBuilder.buildURL2("measurements", aMeasurement().getId()).toString(),responseEntity.getHeaders().get("Location").get(0));
            assertEquals(HttpStatus.CREATED.value(),responseEntity.getStatusCode().value());

        } catch (MeterNotExistsException e) {
            fail(e);
        }

    }
    @Test
    public void getByAddressForDateRange() {

        Page<Measurement> measurementPage = aMeasurementPage();
        try {
            Mockito.when(measurementService.getAllByAddressAndDateBetween(any(),any(),any(),any())).thenReturn(aMeasurementPage());

            ResponseEntity<List<MeasurementDto>> responseEntity = measurementBackController.getByAddressForDateRange(1, LocalDateTime.of(2021,5,5,0,0,0),LocalDateTime.of(2021,5,9,0,0,0),1,1);

            assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
            assertEquals(measurementPage.getContent().size(),responseEntity.getBody().size());
            assertEquals(measurementPage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
            assertEquals(measurementPage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));

        } catch (AddressNotExistsException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllMeasurement() {

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

    @Test
    public void addMeterToMeasurement() throws Exception{
        when(measurementService.addMeterToMeasurement(any(),any())).thenReturn(aMeasurement());

        ResponseEntity<Response> responseEntity = measurementBackController.addMeterToMeasurement(1,1);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        assertEquals(messageResponse("The Measurement has been modified"), responseEntity.getBody());
    }

    @Test
    public void deleteMeasurementById() {
        try {
            doNothing().when(measurementService).deleteMeasurementById(anyInt());

            ResponseEntity<Object> responseEntity = measurementBackController.deleteMeasurementById(1);

            assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
        } catch (MeasurementNotExistsException | RestrictDeleteException e) {
            fail(e);
        }

    }

}
