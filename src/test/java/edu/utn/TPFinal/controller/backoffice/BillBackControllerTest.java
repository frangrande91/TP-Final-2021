package edu.utn.TPFinal.controller.backoffice;

import edu.utn.TPFinal.controller.backoffice.BillBackController;
import edu.utn.TPFinal.exception.alreadyExists.AddressAlreadyExistsException;
import edu.utn.TPFinal.exception.notFound.AddressNotExistsException;
import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.model.dto.BillDto;
import edu.utn.TPFinal.model.response.Response;
import edu.utn.TPFinal.service.BillService;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static edu.utn.TPFinal.utils.AddressTestUtils.aAddress;
import static edu.utn.TPFinal.utils.BillTestUtils.*;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeter;
import static edu.utn.TPFinal.utils.UserTestUtils.aUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class BillBackControllerTest {

    private static BillService billService;
    private static ConversionService conversionService;
    private static BillBackController billBackController;


    @BeforeEach
    public void setUp(){
        billService = mock(BillService.class);
        conversionService = mock(ConversionService.class);
        billBackController = new BillBackController(billService, conversionService);
    }

    @Test
    public void getAllUnpaidByAddress()  {
            Pageable pageable = PageRequest.of(1,  1);
            Page<Bill> pageBill = aBillPage();

        try {
            when(billService.getAllUnpaidByAddress(1,pageable)).thenReturn(pageBill);
            ResponseEntity<List<BillDto>> responseEntity = billBackController.getAllUnpaidByAddress(1, 1,1);

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(pageBill.getContent().size(), responseEntity.getBody().size());
            assertEquals(Long.toString(pageBill.getTotalElements()), (responseEntity.getHeaders().get("X-Total-Count").get(0)));
            assertEquals(Long.toString(pageBill.getTotalPages()), (responseEntity.getHeaders().get("X-Total-Pages").get(0)));

        } catch (AddressNotExistsException e) {
           fail(e);
        }
    }

    @Test
    public void getAllUnpaidByAddressSort()  {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"expiration"));
        orders.add(new Sort.Order(Sort.Direction.DESC,"id"));
        Pageable pageable = PageRequest.of(1,1,Sort.by(orders));
        Page<Bill> pageBill = aBillPage();

        try {
            when(billService.getAllUnpaidByAddress(1,pageable)).thenReturn(pageBill);
            ResponseEntity<List<BillDto>> responseEntity = billBackController.getAllUnpaidByAddressSort(1, 1,1,"expiration","id");

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(pageBill.getContent().size(), responseEntity.getBody().size());
            assertEquals(Long.toString(pageBill.getTotalElements()), (responseEntity.getHeaders().get("X-Total-Count").get(0)));
            assertEquals(Long.toString(pageBill.getTotalPages()), (responseEntity.getHeaders().get("X-Total-Pages").get(0)));

        } catch (AddressNotExistsException e) {
            fail(e);
        }
    }

    @Test
    public void addBill() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Mockito.when(billService.addBill(aBill())).thenReturn(aBill());
        ResponseEntity<Response> responseEntity = billBackController.addBill(aBill());

        assertEquals(EntityURLBuilder.buildURL2("bills", aBill().getId()).toString(),responseEntity.getHeaders().get("Location").get(0));
        assertEquals(HttpStatus.CREATED.value(),responseEntity.getStatusCode().value());

    }

    @Test
    public void getAllBillsOk() throws Exception{
        Pageable pageable = PageRequest.of(1,  1);
        Page<Bill> pageBill = aBillPage();
        when(billService.getAllBills(pageable)).thenReturn(pageBill);

        ResponseEntity<List<BillDto>> responseEntity = billBackController.getAllBills(1, 1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(pageBill.getContent().size(), responseEntity.getBody().size());
        assertEquals(Long.toString(pageBill.getTotalElements()), (responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(Long.toString(pageBill.getTotalPages()), (responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }

    /*@Test
    public void getAllBillsNoContent() {
        Pageable pageable = PageRequest.of(1,  1);
        Page<Bill> pageBill = aBillPageEmpty();
        when(billService.getAllBills(pageable)).thenReturn(pageBill);

        ResponseEntity<List<BillDto>> responseEntity = billBackController.getAllBills(pageable.getPageSize(), pageable.getPageNumber());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody().size());
    }*/

    @Test
    public void getAllSorted() {

        Pageable pageable = PageRequest.of(1, 1);
        Page<Bill> billPage = aBillPage();
        when(billService.getAllSort(any(), any(), anyList())).thenReturn(billPage);

        ResponseEntity<List<BillDto>> responseEntity = billBackController.getAllSorted(pageable.getPageSize(), pageable.getPageNumber(), "id", "user");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(billPage.getContent().size(), responseEntity.getBody().size());
        assertEquals(Long.toString(billPage.getTotalPages()), (responseEntity.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals(Long.toString(billPage.getTotalElements()), (responseEntity.getHeaders().get("X-Total-Count").get(0)));
    }

    @Test
    public void getAllSpec() throws Exception {
        Specification<Bill> billSpecification = Mockito.mock(Specification.class);
        Page<Bill> billPage  = aBillPage();
        Pageable pageable = PageRequest.of(1,1);
        Mockito.when(billService.getAllSpec(billSpecification, pageable)).thenReturn(billPage);

        ResponseEntity<List<BillDto>> responseEntity = billBackController.getAllSpec(billSpecification,pageable);

        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(billPage.getContent().size(),responseEntity.getBody().size());
        assertEquals(billPage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(billPage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }


    @Test
    public void getBillById() throws Exception{
        when(billService.getBillById(anyInt())).thenReturn(aBill());
        when(conversionService.convert(aBill(), BillDto.class)).thenReturn(aBillDto());

        ResponseEntity<BillDto> responseEntity = billBackController.getBillById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(aBillDto(), responseEntity.getBody());
    }



    @Test
    public void addClientToBill() throws Exception{
        when(billService.addClientToBill(aBill().getId(), aUser().getId())).thenReturn(aBill());

        ResponseEntity<Response> responseEntity = billBackController.addClientToBill(1,1);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void addAddressToBill() throws Exception{
        when(billService.addAddressToBill(aBill().getId(), aAddress().getId())).thenReturn(aBill());

        ResponseEntity<Response> responseEntity = billBackController.addAddressToBill(1,1);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void addMeterToBill() throws Exception{
        when(billService.addMeterToBill(aBill().getId(), aMeter().getId())).thenReturn(aBill());

        ResponseEntity<Response> responseEntity = billBackController.addMeterToBill(1,1);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void deleteBillById() throws Exception{
        doNothing().when(billService).deleteBillById(anyInt());

        ResponseEntity<Object> responseEntity = billBackController.deleteBillById(1);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

}
