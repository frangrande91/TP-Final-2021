package edu.utn.TPFinal.controller.app;

import edu.utn.TPFinal.model.dto.BillDto;
import edu.utn.TPFinal.service.BillService;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static edu.utn.TPFinal.utils.BillTestUtils.*;
import static edu.utn.TPFinal.utils.UserTestUtils.aUserDto;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BillAppControllerTest {

    private static BillService billService;
    private static ConversionService conversionService;
    private static BillAppController billAppController;

    @BeforeAll
    public static void setUp(){
        billService = mock(BillService.class);
        conversionService = mock(ConversionService.class);
        billAppController = new BillAppController(billService, conversionService);
    }

    @Test
    public void getAllByUserClientAndBetweenDateOk() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(aUserDto());
        Pageable pageable = PageRequest.of(1,1);
        when(billService.getAllBillsByUserClientAndBetweenDate(1, 1, LocalDate.of(2020, 01, 05),  LocalDate.of(2020, 12, 05), pageable)).thenReturn(aBillPage());
        when(conversionService.convert(aBill(), BillDto.class)).thenReturn(aBillDto());

        try {
            LocalDate from = LocalDate.parse("2020-01-05", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate to = LocalDate.parse("2020-12-05", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            ResponseEntity<List<BillDto>> response = billAppController.getAllByUserClientAndBetweenDate(1, 1,1, from, to, authentication);

            Assert.assertEquals(aBillDtoPage().getContent().size(),response.getBody().size());
            Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assert.assertEquals(Long.toString(aBillDtoPage().getTotalElements()), (response.getHeaders().get("X-Total-Count").get(0)));
            Assert.assertEquals(Long.toString(aBillDtoPage().getTotalPages()), (response.getHeaders().get("X-Total-Pages").get(0)));
        } catch (DateTimeParseException e) {
            fail(e);
        }
    }

    /*
    @Test
    public void getAllByUserClientAndBetweenDateNoContent() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(aUserDto());
        Pageable pageable = PageRequest.of(1, 1);
        when(billService.getAllBillsByUserClientAndBetweenDate(1, 1, LocalDate.of(2020, 01, 05), LocalDate.of(2020, 12, 05), pageable)).thenReturn(aBillPageEmpty());
        when(conversionService.convert(aBill(), BillDto.class)).thenReturn(aBillDto());

        try {
            LocalDate from = LocalDate.parse("2020-01-05", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate to = LocalDate.parse("2020-12-05", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            ResponseEntity<List<BillDto>> response = billAppController.getAllByUserClientAndBetweenDate(1, 1, 1, from, to, authentication);

            Assert.assertEquals(0, response.getBody().size());
            Assert.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        } catch (DateTimeParseException e) {
            fail(e);
        }
    }

     */

    @Test
    public void getAllSortsByUserClientAndBetweenDate() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(aUserDto());
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "id"));
        orders.add(new Sort.Order(Sort.Direction.DESC, "user"));
        Pageable pageable = PageRequest.of(1, 1, Sort.by(orders));
        when(billService.getAllBillsByUserClientAndBetweenDate(1, 1, LocalDate.of(2020, 01, 05), LocalDate.of(2020, 12, 05), pageable)).thenReturn(aBillPage());
        when(conversionService.convert(aBill(), BillDto.class)).thenReturn(aBillDto());

        try {
            LocalDate from = LocalDate.parse("2020-01-05", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate to = LocalDate.parse("2020-12-05", DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            ResponseEntity<List<BillDto>> response = billAppController.getAllSortsByUserClientBetweenDate(1, 1, 1, "id", "user",from, to, authentication);

            Assert.assertEquals(aBillDtoPage().getContent().size(), response.getBody().size());
            Assert.assertEquals(aBillDtoPage().getContent().get(0), response.getBody().get(0));
            Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assert.assertEquals(Long.toString(aBillDtoPage().getTotalElements()), (response.getHeaders().get("X-Total-Count").get(0)));
            Assert.assertEquals(Long.toString(aBillDtoPage().getTotalPages()), (response.getHeaders().get("X-Total-Pages").get(0)));
        } catch (DateTimeParseException e) {
            fail(e);
        }
    }


    @Test
    public void getAllUnpaidByUserClientOk() throws Exception {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(aUserDto());
        Pageable pageable = PageRequest.of(1, 1);

        when(billService.getAllUnpaidByUserClient(1,2, pageable)).thenReturn(aBillPage());
        when(conversionService.convert(aBill(), BillDto.class)).thenReturn(aBillDto());

        ResponseEntity<List<BillDto>> response = billAppController.getAllUnpaidByUserClient(2,1, 1, authentication);

        Assert.assertEquals(aBillDtoPage().getContent().size(),response.getBody().size());
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertEquals(Long.toString(aBillDtoPage().getTotalElements()), (response.getHeaders().get("X-Total-Count").get(0)));
        Assert.assertEquals(Long.toString(aBillDtoPage().getTotalPages()), (response.getHeaders().get("X-Total-Pages").get(0)));
    }




}

