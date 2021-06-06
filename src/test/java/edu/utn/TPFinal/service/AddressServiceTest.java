package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.alreadyExists.AddressAlreadyExistsException;
import edu.utn.TPFinal.exceptions.notFound.AddressNotExistsException;
import edu.utn.TPFinal.exceptions.notFound.MeterNotExistsException;
import edu.utn.TPFinal.exceptions.notFound.RateNotExistsException;
import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.repository.AddressRepository;
import edu.utn.TPFinal.repository.MeterRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.utn.TPFinal.utils.AddressTestUtils.*;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeter;
import static edu.utn.TPFinal.utils.RateTestUtils.aRate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class AddressServiceTest {

    private static AddressRepository addressRepository;
    private static MeterRepository meterRepository;
    private static MeterService meterService;
    private static RateService rateService;
    private static AddressService addressService;

    @BeforeAll
    public static void setUp(){
        addressRepository = mock(AddressRepository.class);
        meterRepository = mock(MeterRepository.class);
        meterService = mock(MeterService.class);
        rateService = mock(RateService.class);
        addressService = new AddressService(addressRepository, meterService, rateService);
    }

    @AfterEach
    public void after() {
        Mockito.reset(addressRepository);
        Mockito.reset(meterService);
        Mockito.reset(rateService);
    }

    @Test
    public void addAddressOk() throws Exception{
        //given
        when(addressRepository.findByIdOrAddress(anyInt(), anyString())).thenReturn(null);
        when(addressRepository.save(aAddress())).thenReturn(aAddress());

        //when
        Address address = addressService.addAddress(aAddress());

        //then
        assertEquals(aAddress(), address);
        verify(addressRepository, times(1)).findByIdOrAddress(anyInt(), anyString());
        verify(addressRepository, times( 1)).save(address);
    }

    @Test
    public void addAddressAlreadyExists(){
        //given
        when(addressRepository.findByIdOrAddress(anyInt(), anyString())).thenReturn(aAddress());

        //when and then
        assertThrows(AddressAlreadyExistsException.class, () -> addressService.addAddress(aAddress()));
        verify(addressRepository, times(1)).findByIdOrAddress(anyInt(), anyString());
        verify(addressRepository, times( 0)).save(aAddress());
    }

    @Test
    public void getAllAddress(){
        //given
        Pageable pageable = PageRequest.of(1, 1);
        when(addressRepository.findAll(pageable)).thenReturn(aAddressPage());

        //when
        Page<Address> addressPage = addressService.getAllAddress(pageable);

        //then
        assertEquals(aAddressPage().getTotalElements(), addressPage.getTotalElements());
        assertEquals(aAddressPage().getTotalPages(), addressPage.getTotalPages());
        assertEquals(aAddressPage().getContent(), addressPage.getContent());
        verify(addressRepository, times(1)).findAll(pageable);
    }

    @Test
    public void getAllSpec(){
        //given
        Pageable pageable = PageRequest.of(1,1);
        Specification<Address> addressSpecification = aSpecAddress("False Street 123");
        when(addressRepository.findAll(addressSpecification, pageable)).thenReturn(aAddressPage()) ;

        //when
        Page<Address> addressPage = addressService.getAllSpec(addressSpecification, pageable);

        //then
        assertEquals(aAddressPage().getTotalElements(), addressPage.getTotalElements());
        assertEquals(aAddressPage().getTotalPages(), addressPage.getTotalPages());
        assertEquals(aAddressPage().getContent(), addressPage.getContent());
        verify(addressRepository, times(1)).findAll(addressSpecification, pageable);
    }

    @Test
    public void getAllSort(){
        //given
        List<Sort.Order> orders = new ArrayList<>();
        Pageable pageable = PageRequest.of(1,1, Sort.by(orders));
        when(addressRepository.findAll(pageable)).thenReturn(aAddressPage());

        //when
        Page<Address> addressPage = addressService.getAllSort(1, 1, orders);

        //then
        assertEquals(aAddressPage().getTotalElements(), addressPage.getTotalElements());
        assertEquals(aAddressPage().getTotalPages(), addressPage.getTotalPages());
        assertEquals(aAddressPage().getContent(), addressPage.getContent());
        verify(addressRepository, times(1)).findAll(pageable);
    }

    @Test
    public void getAddressByIdOk(){
        //given
        when(addressRepository.findById(1)).thenReturn(Optional.of(aAddress()));

        try {
            //when
            Address address = addressService.getAddressById(1);

            //then
            assertEquals(aAddress(), address);
            verify(addressRepository, times(1)).findById(1);
        } catch (AddressNotExistsException e) {
            fail(e);
        }
    }

    @Test
    public void getAddressByIdNotExists(){
        //given
        when(addressRepository.findById(1)).thenReturn(Optional.empty());

        //when and then
        assertThrows(AddressNotExistsException.class, () -> addressService.getAddressById(1));
        verify(addressRepository, times(1)).findById(1);
    }

    @Test
    public void addMeterToAddressOk(){
        try {
            //given
            Address address = aAddress();
            address.setMeter(aMeter());
            when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
            when(meterService.getMeterById(aMeter().getId())).thenReturn(aMeter());
            when(addressRepository.save(address)).thenReturn(address);

            //when
            Address addressAux = addressService.addMeterToAddress(1,1);

            //then
            assertEquals(address.getMeter(), addressAux.getMeter());
            verify(addressRepository, times(1)).findById(address.getId());
            verify(meterService, times(1)).getMeterById(1);
            verify(addressRepository, times(1)).save(address);
        } catch (AddressNotExistsException | MeterNotExistsException e) {
            fail(e);
        }
    }

    /*
    @Test
    public void addMeterToAddressMeterNotExists(){
        when(meterRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(MeterNotExistsException.class, () -> addressService.addMeterToAddress(1,1) );
        verify(meterRepository, times(1)).findById(1);
    }
     */

    @Test
    public void addMeterToAddressAddressNotExist(){
        //given
        when(addressRepository.findById(aAddress().getId())).thenReturn(Optional.empty());

        //when and then
        assertThrows(AddressNotExistsException.class, () -> addressService.addMeterToAddress(aAddress().getId(),aMeter().getId()) );
        verify(addressRepository, times(1)).findById(aAddress().getId());
    }

    @Test
    public void addRateToAddressOk(){
        try {
            //given
            Address address = aAddress();
            address.setRate(aRate());
            when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
            when(rateService.getRateById(aRate().getId())).thenReturn(aRate());
            when(addressRepository.save(address)).thenReturn(address);

            //when
            Address address1 = addressService.addRateToAddress(address.getId(), aRate().getId());

            //then
            assertEquals(address.getRate(), address1.getRate());
            verify(addressRepository, times(1)).findById(address.getId());
            verify(rateService, times(1)).getRateById(aRate().getId());
            verify(addressRepository, times(1)).save(address);
        }
        catch (AddressNotExistsException | RateNotExistsException e){
            fail(e);
        }
    }

    @Test
    public void addRateToAddressNotExists(){
        //given
        when(addressRepository.findById(aAddress().getId())).thenReturn(Optional.empty());

        //when and then
        assertThrows(AddressNotExistsException.class, () -> addressService.addMeterToAddress(aAddress().getId(),aMeter().getId()) );
        verify(addressRepository, times(1)).findById(aAddress().getId());
    }

    @Test
    public void deleteAddressByIdOk(){
        try {
            //given
            when(addressRepository.findById(aAddress().getId())).thenReturn(Optional.of(aAddress()));
            doNothing().when(addressRepository).deleteById(aAddress().getId());

            //when
            addressService.deleteAddressById(aAddress().getId());

            //then
            when(addressRepository.findById(aAddress().getId())).thenReturn(Optional.empty());
            verify(addressRepository, times(1)).findById(aAddress().getId());
            verify(addressRepository, times(1)).deleteById(aAddress().getId());
        }
        catch (AddressNotExistsException e){
            fail(e);
        }
    }

    @Test
    public void deleteAddressByIdAddressNotExists(){
        //given
        when(addressRepository.findById(aAddress().getId())).thenReturn(Optional.empty());

        //when and then
        assertThrows(AddressNotExistsException.class, () -> addressService.deleteAddressById(aAddress().getId()));
        verify(addressRepository, times(1)).findById(aAddress().getId());
    }




}
