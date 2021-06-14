package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.controller.backoffice.AddressBackController;
import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.dto.AddressDto;
import edu.utn.TPFinal.model.response.Response;
import edu.utn.TPFinal.service.AddressService;
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

import static edu.utn.TPFinal.utils.AddressTestUtils.*;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AddressControllerTest {

    private static AddressService addressService;
    private static ConversionService conversionService;
    private static AddressBackController addressBackController;

    @BeforeAll
    public static void setUp(){
        addressService = mock(AddressService.class);
        conversionService = mock(ConversionService.class);
        addressBackController = new AddressBackController(addressService, conversionService);
    }

    //agregar add

    @Test
    public void getAllAddressesOk() throws Exception{
        //given
        Pageable pageable = PageRequest.of(1,  10);
        Page<Address> pageAddress = aAddressPage();
        when(addressService.getAllAddress(pageable)).thenReturn(pageAddress);

        //when
        ResponseEntity<List<AddressDto>> responseEntity = addressBackController.getAllAddresses(10, 1);

        //then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(pageAddress.getContent().size(), responseEntity.getBody().size());
        assertEquals(Long.toString(pageAddress.getTotalElements()), (responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(Long.toString(pageAddress.getTotalPages()), (responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }


    @Test
    public void getAllAddressesNoContent() throws Exception{
        //given
        Pageable pageable = PageRequest.of(1,  1);
        Page<Address> pageAddress = aAddressPageEmpty();
        when(addressService.getAllAddress(pageable)).thenReturn(pageAddress);

        //when
        ResponseEntity<List<AddressDto>> responseEntity = addressBackController.getAllAddresses(pageable.getPageSize(), pageable.getPageNumber());

        //then
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody().size());
    }

    @Test
    public void getAllSorted() throws Exception{
        //given
        Pageable pageable = PageRequest.of(1, 1);
        Page<Address> addressPage = aAddressPage();
        when(addressService.getAllSort(any(), any(), anyList())).thenReturn(addressPage);

        //when
        ResponseEntity<List<AddressDto>> responseEntity = addressBackController.getAllSorted(pageable.getPageSize(), pageable.getPageNumber(), "id", "address");

        //then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(addressPage.getContent().size(), responseEntity.getBody().size());
        assertEquals(Long.toString(addressPage.getTotalPages()), (responseEntity.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals(Long.toString(addressPage.getTotalElements()), (responseEntity.getHeaders().get("X-Total-Count").get(0)));
    }


    @Test
    public void getAllSpec() throws Exception {
        //given
        Specification<Address> addressSpecification = Mockito.mock(Specification.class);
        Page<Address> addressPage  = aAddressPage();
        Pageable pageable = PageRequest.of(1,1);
        Mockito.when(addressService.getAllSpec(addressSpecification, pageable)).thenReturn(addressPage);

        //when
        ResponseEntity<List<AddressDto>> responseEntity = addressBackController.getAllSpec(addressSpecification,pageable);

        //then
        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(addressPage.getContent().size(),responseEntity.getBody().size());
        assertEquals(addressPage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(addressPage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }

    @Test
    public void getAddressById() throws Exception{
        //given
        when(addressService.getAddressById(anyInt())).thenReturn(aAddress());
        when(conversionService.convert(aAddress(), AddressDto.class)).thenReturn(aAddressDto());

        //when
        ResponseEntity<AddressDto> responseEntity = addressBackController.getAddressById(1);

        //then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(aAddressDto(), responseEntity.getBody());
    }

    @Test
    public void addMeterToAddress() throws Exception {
        //given
        when(addressService.addMeterToAddress(aAddress().getId(), aMeter().getId())).thenReturn(aAddress());

        //when
        ResponseEntity<Response> responseEntity = addressBackController.addMeterToAddress(1,1);

        //then
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void addRateToAddress() throws Exception{
        //given
        when(addressService.addRateToAddress(anyInt(), anyInt())).thenReturn(aAddress());

        //when
        ResponseEntity<Response> responseEntity = addressBackController.addRateToAddress(1,1);

        //then
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());

    }

    @Test
    public void deleteAddressById() throws Exception{
        //given
        doNothing().when(addressService).deleteAddressById(anyInt());

        //when
        ResponseEntity<Object> responseEntity = addressBackController.deleteAddressById(1);

        //then
        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

}
