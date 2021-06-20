package edu.utn.TPFinal.controller.app;

import edu.utn.TPFinal.model.dto.MeasurementDto;
import edu.utn.TPFinal.model.response.ClientConsumption;
import edu.utn.TPFinal.service.MeasurementService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import static edu.utn.TPFinal.utils.MeasurementTestUtils.*;
import static edu.utn.TPFinal.utils.UserTestUtils.aUserDto;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
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
        when(measurementService.getConsumptionByMeterAndDateBetween(1, aUserDto().getId(), LocalDateTime.of(2021,5,5,0,0,0),LocalDateTime.of(2021,5,9,0,0,0))).thenReturn(aClientConsumption());
        when(measurementService.getConsumptionByMeterAndDateBetween(any(), any(),any(),any())).thenReturn(aClientConsumption());

        try {

            LocalDateTime from = LocalDateTime.parse("2020-01-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime to = LocalDateTime.parse("2020-12-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            ResponseEntity<ClientConsumption> response = measurementAppController.getConsumptionByMeter(1, from, to, authentication);

            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertEquals(aClientConsumption(), response.getBody());

        } catch (DateTimeParseException e) {
            fail(e);
        }
    }


    @Test
    public void getAllByMeter() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(aUserDto());
        Pageable pageable = PageRequest.of(1, 1);
        when(measurementService.getAllByMeterAndDateBetween(1, aUserDto().getId(), LocalDateTime.of(2021,5,5,0,0,0),LocalDateTime.of(2021,5,9,0,0,0), pageable)).thenReturn(aMeasurementPage());
        when(conversionService.convert(aMeasurement(), MeasurementDto.class)).thenReturn(aMeasurementDto());

        try {

            LocalDateTime from = LocalDateTime.parse("2021-05-05 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            LocalDateTime to = LocalDateTime.parse("2021-05-09 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            ResponseEntity<List<MeasurementDto>> response = measurementAppController.getAllByMeter(1, from, to, 1,1, authentication);

            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertEquals(aMeasurementDtoPage().getContent().size(), response.getBody().size());

        } catch (DateTimeParseException e) {
            fail(e);
        }
    }

}




