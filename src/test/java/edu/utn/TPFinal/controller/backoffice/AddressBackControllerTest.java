package edu.utn.TPFinal.controller.backoffice;

import edu.utn.TPFinal.controller.backoffice.AddressBackController;
import edu.utn.TPFinal.exception.ViolationChangeKeyAttributeException;
import edu.utn.TPFinal.exception.alreadyExists.AddressAlreadyExistsException;
import edu.utn.TPFinal.exception.notFound.AddressNotExistsException;
import edu.utn.TPFinal.exception.notFound.RateNotExistsException;
import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.dto.AddressDto;
import edu.utn.TPFinal.model.response.Response;
import edu.utn.TPFinal.service.AddressService;
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

import java.util.List;

import static edu.utn.TPFinal.utils.AddressTestUtils.*;
import static edu.utn.TPFinal.utils.EntityResponse.messageResponse;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeter;
import static edu.utn.TPFinal.utils.RateTestUtils.aRate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AddressBackControllerTest {

    private static AddressService addressService;
    private static ConversionService conversionService;
    private static AddressBackController addressBackController;

    @BeforeAll
    public static void setUp(){
        addressService = mock(AddressService.class);
        conversionService = mock(ConversionService.class);
        addressBackController = new AddressBackController(addressService, conversionService);
    }


    @Test
    public void addAddress() {

        try {
            MockHttpServletRequest request = new MockHttpServletRequest();
            RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

            Mockito.when(addressService.addAddress(aAddress())).thenReturn(aAddress());
            ResponseEntity<Response> responseEntity = addressBackController.addAddress(aAddress());

            assertEquals(EntityURLBuilder.buildURL2("addresses", aAddress().getId()).toString(),responseEntity.getHeaders().get("Location").get(0));
            assertEquals(HttpStatus.CREATED.value(),responseEntity.getStatusCode().value());
        }
        catch (AddressAlreadyExistsException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAllAddressesOk() throws Exception{
        Pageable pageable = PageRequest.of(1,  10);
        Page<Address> pageAddress = aAddressPage();
        when(addressService.getAllAddress(pageable)).thenReturn(pageAddress);

        ResponseEntity<List<AddressDto>> responseEntity = addressBackController.getAllAddresses(10, 1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(pageAddress.getContent().size(), responseEntity.getBody().size());
        assertEquals(Long.toString(pageAddress.getTotalElements()), (responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(Long.toString(pageAddress.getTotalPages()), (responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }


    @Test
    public void getAllAddressesNoContent() throws Exception{
        Pageable pageable = PageRequest.of(1,  1);
        Page<Address> pageAddress = aAddressPageEmpty();
        when(addressService.getAllAddress(pageable)).thenReturn(pageAddress);

        ResponseEntity<List<AddressDto>> responseEntity = addressBackController.getAllAddresses(pageable.getPageSize(), pageable.getPageNumber());

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody().size());
    }

    @Test
    public void getAllSorted() throws Exception{
        Pageable pageable = PageRequest.of(1, 1);
        Page<Address> addressPage = aAddressPage();
        when(addressService.getAllSort(any(), any(), anyList())).thenReturn(addressPage);

        ResponseEntity<List<AddressDto>> responseEntity = addressBackController.getAllSorted(pageable.getPageSize(), pageable.getPageNumber(), "id", "address");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(addressPage.getContent().size(), responseEntity.getBody().size());
        assertEquals(Long.toString(addressPage.getTotalPages()), (responseEntity.getHeaders().get("X-Total-Pages").get(0)));
        assertEquals(Long.toString(addressPage.getTotalElements()), (responseEntity.getHeaders().get("X-Total-Count").get(0)));
    }


    @Test
    public void getAllSpec() throws Exception {
        Specification<Address> addressSpecification = Mockito.mock(Specification.class);
        Page<Address> addressPage  = aAddressPage();
        Pageable pageable = PageRequest.of(1,1);
        Mockito.when(addressService.getAllSpec(addressSpecification, pageable)).thenReturn(addressPage);

        ResponseEntity<List<AddressDto>> responseEntity = addressBackController.getAllSpec(addressSpecification,pageable);


        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(addressPage.getContent().size(),responseEntity.getBody().size());
        assertEquals(addressPage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(addressPage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }

    @Test
    public void getAddressById() throws Exception{
        when(addressService.getAddressById(anyInt())).thenReturn(aAddress());
        when(conversionService.convert(aAddress(), AddressDto.class)).thenReturn(aAddressDto());

        ResponseEntity<AddressDto> responseEntity = addressBackController.getAddressById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(aAddressDto(), responseEntity.getBody());
    }

    @Test
    public void addMeterToAddress() throws Exception {
        when(addressService.addMeterToAddress(aAddress().getId(), aMeter().getId())).thenReturn(aAddress());

        ResponseEntity<Response> responseEntity = addressBackController.addMeterToAddress(1,1);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void addRateToAddress() throws Exception{
        when(addressService.addRateToAddress(anyInt(), anyInt())).thenReturn(aAddress());

        ResponseEntity<Response> responseEntity = addressBackController.addRateToAddress(1,1);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());

    }

    @Test
    public void deleteAddressById() throws Exception{
        doNothing().when(addressService).deleteAddressById(anyInt());

        ResponseEntity<Object> responseEntity = addressBackController.deleteAddressById(1);

        assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
    }

    @Test
    public void updateAddress() {
        try {
            Mockito.when(addressService.updateAddress(any(),any())).thenReturn(aAddress());

            ResponseEntity<Response> responseEntity = addressBackController.updateAddress(1,aAddress());

            assertEquals(HttpStatus.ACCEPTED.value(),responseEntity.getStatusCode().value());
            assertEquals(messageResponse("The address has been updated"),responseEntity.getBody());

        }
        catch ( ViolationChangeKeyAttributeException | AddressNotExistsException e) {
            fail(e);
        }
    }

}
