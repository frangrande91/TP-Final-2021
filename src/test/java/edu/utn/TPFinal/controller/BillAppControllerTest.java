package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.controller.backoffice.BillBackController;
import edu.utn.TPFinal.service.BillService;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.core.convert.ConversionService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BillAppControllerTest {

    private static BillService billService;
    private static ConversionService conversionService;
    private static BillBackController billBackController;


    @BeforeAll
    public static void setUp(){
        billService = mock(BillService.class);
        conversionService = mock(ConversionService.class);
        billBackController = new BillBackController(billService, conversionService);
    }

    /*@Test
    public void getAllBillsOk() throws Exception{
        //given
        Pageable pageable = PageRequest.of(1,  1);
        Page<Bill> pageBill = aBillPage();
        when(billService.getAllBills(pageable)).thenReturn(pageBill);

        //when
        ResponseEntity<List<BillDto>> responseEntity = billBackController.getAllBills(1, 1);

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
        ResponseEntity<List<BillDto>> responseEntity = billBackController.getAllBills(pageable.getPageSize(), pageable.getPageNumber());

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
        ResponseEntity<List<BillDto>> responseEntity = billBackController.getAllSorted(pageable.getPageSize(), pageable.getPageNumber(), "id", "user");

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
        ResponseEntity<List<BillDto>> responseEntity = billBackController.getAllSpec(billSpecification,pageable);

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
        ResponseEntity<BillDto> responseEntity = billBackController.getBillById(1);

        //then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(aBillDto(), responseEntity.getBody());
    }


    @Test
    public void addClientToBill() throws Exception{
        //given
        when(billService.addClientToBill(aBill().getId(), aUser().getId())).thenReturn(aBill());

        //when
        ResponseEntity<Response> responseEntity = billBackController.addClientToBill(1,1);

        //then
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void addAddressToBill() throws Exception{
        //given
        when(billService.addAddressToBill(aBill().getId(), aAddress().getId())).thenReturn(aBill());

        //when
        ResponseEntity<Response> responseEntity = billBackController.addAddressToBill(1,1);

        //then
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void addMeterToBill() throws Exception{
        //given
        when(billService.addMeterToBill(aBill().getId(), aMeter().getId())).thenReturn(aBill());

        //when
        ResponseEntity<Response> responseEntity = billBackController.addMeterToBill(1,1);

        //then
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());

    }

    @Test
    public void deleteBillById() throws Exception{
        //given
        doNothing().when(billService).deleteBillById(anyInt());

        //when
        ResponseEntity<Object> responseEntity = billBackController.deleteBillById(1);

        //then
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

*/

}
