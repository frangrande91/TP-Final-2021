package edu.utn.TPFinal.controller.app;

import edu.utn.TPFinal.model.dto.MeasurementDto;
import edu.utn.TPFinal.model.response.ClientConsumption;
import edu.utn.TPFinal.service.MeasurementService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static edu.utn.TPFinal.utils.MeasurementTestUtils.*;
import static edu.utn.TPFinal.utils.UserTestUtils.aUserDto;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MeasurementAppControllerTest {

    private static MeasurementService measurementService;
    private static ConversionService conversionService;
    private static MeasurementAppController measurementAppController;

    @BeforeAll
    public static void setUp() {
        measurementService = mock(MeasurementService.class);
        conversionService = mock(ConversionService.class);
        measurementAppController = new MeasurementAppController(measurementService, conversionService);
    }

    @Test
    public void getConsumptionByMeter() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(aUserDto());
        when(measurementService.getConsumptionByMeterAndDateBetween(1, aUserDto().getId(), LocalDate.of(2020, 01, 05), LocalDate.of(2020, 12, 05))).thenReturn(aClientConsumption());

        try {

            LocalDate from = LocalDate.parse("2020-01-05", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate to = LocalDate.parse("2020-12-05", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            ResponseEntity<ClientConsumption> response = measurementAppController.getConsumptionByMeter(1, from, to, authentication);

            Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assert.assertEquals(aClientConsumption(), response.getBody());

        } catch (DateTimeParseException e) {
            fail(e);
        }
    }


    @Test
    public void getAllByMeter() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(aUserDto());
        Pageable pageable = PageRequest.of(1, 1);
        when(measurementService.getAllByMeterAndDateBetween(1, aUserDto().getId(), LocalDate.of(2020, 01, 05), LocalDate.of(2020, 12, 05), pageable)).thenReturn(aMeasurementPage());
        when(conversionService.convert(aMeasurement(), MeasurementDto.class)).thenReturn(aMeasurementDto());

        try {

            LocalDate from = LocalDate.parse("2020-01-05", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate to = LocalDate.parse("2020-12-05", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            ResponseEntity<List<MeasurementDto>> response = measurementAppController.getAllByMeter(1, from, to, 1,1, authentication);

            Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assert.assertEquals(aMeasurementDtoPage().getContent().size(), response.getBody().size());

        } catch (DateTimeParseException e) {
            fail(e);
        }
    }

}




