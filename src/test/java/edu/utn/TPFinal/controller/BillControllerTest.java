package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.model.dto.BillDto;
import edu.utn.TPFinal.model.responses.Response;
import edu.utn.TPFinal.service.BillService;
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

import java.util.List;

import static edu.utn.TPFinal.utils.BillTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BillControllerTest {

    private static BillService billService;
    private static ConversionService conversionService;
    private static BillController billController;


    @BeforeAll
    public static void setUp(){
        billService = mock(BillService.class);
        conversionService = mock(ConversionService.class);
        billController = new BillController(billService, conversionService);
    }

    @Test
    public void getAllBillsOk() throws Exception{
        //given
        Pageable pageable = PageRequest.of(1,  1);
        Page<Bill> pageBill = aBillPage();
        when(billService.getAllBills(pageable)).thenReturn(pageBill);

        //when
        ResponseEntity<List<BillDto>> responseEntity = billController.getAllBills(1, 1);

        //then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(pageBill.getContent().size(), responseEntity.getBody().size());
        assertEquals(Long.toString(pageBill.getTotalElements()), (responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(Long.toString(pageBill.getTotalPages()), (responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }


    @Test
    public void getAllBillsNoContent() throws Exception{
        //given
        Pageable pageable = PageRequest.of(1,  1);
        Page<Bill> pageBill = aBillPageEmpty();
        when(billService.getAllBills(pageable)).thenReturn(pageBill);

        //when
        ResponseEntity<List<BillDto>> responseEntity = billController.getAllBills(pageable.getPageSize(), pageable.getPageNumber());

        //then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody().size());
    }


    @Test
    public void getAllSorted() throws Exception{
        //given
        Pageable pageable = PageRequest.of(1, 1);
        Page<Bill> billPage = aBillPage();
        when(billService.getAllSort(any(), any(), anyList())).thenReturn(billPage);

        //when
        ResponseEntity<List<BillDto>> responseEntity = billController.getAllSorted(pageable.getPageSize(), pageable.getPageNumber(), "id", "user");

        //then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(billPage.getContent().size(), responseEntity.getBody().size());
        assertEquals(Long.toString(billPage.getTotalPages()), (responseEntity.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals(Long.toString(billPage.getTotalElements()), (responseEntity.getHeaders().get("X-Total-Count").get(0)));
    }

    @Test
    public void getAllSpec() throws Exception {
        //given
        Specification<Bill> billSpecification = Mockito.mock(Specification.class);
        Page<Bill> billPage  = aBillPage();
        Pageable pageable = PageRequest.of(1,1);
        Mockito.when(billService.getAllSpec(billSpecification, pageable)).thenReturn(billPage);

        //when
        ResponseEntity<List<BillDto>> responseEntity = billController.getAllSpec(billSpecification,pageable);

        //then
        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(billPage.getContent().size(),responseEntity.getBody().size());
        assertEquals(billPage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(billPage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }


    @Test
    public void getBillById() throws Exception{
        //given
        when(billService.getBillById(anyInt())).thenReturn(aBill());
        when(conversionService.convert(aBill(), BillDto.class)).thenReturn(aBillDto());

        //when
        ResponseEntity<BillDto> responseEntity = billController.getBillById(1);

        //then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(aBillDto(), responseEntity.getBody());
    }


    @Test
    public void addClientToBill() throws Exception{
        //given
        doNothing().when(billService).addClientToBill(anyInt(), anyInt());

        //when
        ResponseEntity<Response> responseEntity = billController.addClientToBill(1,1);

        //then
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void addAddressToBill() throws Exception{
        //given
        doNothing().when(billService).addAddressToBill(anyInt(), anyInt());

        //when
        ResponseEntity<Response> responseEntity = billController.addAddressToBill(1,1);

        //then
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());

    }

    @Test
    public void addMeterToBill() throws Exception{
        //given
        doNothing().when(billService).addMeterToBill(anyInt(), anyInt());

        //when
        ResponseEntity<Response> responseEntity = billController.addMeterToBill(1,1);

        //then
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());

    }

    @Test
    public void deleteBillById() throws Exception{
        //given
        doNothing().when(billService).deleteBillById(anyInt());

        //when
        ResponseEntity<Object> responseEntity = billController.deleteBillById(1);

        //then
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }



}