package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exception.RestrictDeleteException;
import edu.utn.TPFinal.exception.notFound.AddressNotExistsException;
import edu.utn.TPFinal.exception.notFound.BillNotExistsException;
import edu.utn.TPFinal.exception.notFound.MeterNotExistsException;
import edu.utn.TPFinal.exception.notFound.UserNotExistsException;
import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.repository.BillRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.utn.TPFinal.utils.AddressTestUtils.aAddress;
import static edu.utn.TPFinal.utils.BillTestUtils.*;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeter;
import static edu.utn.TPFinal.utils.UserTestUtils.aUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class BillServiceTest {

    private static BillRepository billRepository;
    private static MeterService meterService;
    private static MeasurementService measurementService;
    private static UserService userService;
    private static AddressService addressService;
    private static BillService billService;

    @BeforeAll
    public static void setUp(){
        billRepository = mock(BillRepository.class);
        meterService = mock(MeterService.class);
        measurementService = mock(MeasurementService.class);
        userService = mock(UserService.class);
        addressService = mock(AddressService.class);
        billService = new BillService(billRepository, meterService, measurementService, userService, addressService);
    }

    @AfterEach
    public void after(){
        reset(billRepository);
        reset(meterService);
        reset(measurementService);
        reset(userService);
        reset(addressService);
    }

    @Test
    public void addBill(){
        when(billRepository.save(aBill())).thenReturn(aBill());

        Bill bill = billService.addBill(aBill());

        assertEquals(aBill(), bill);
        verify(billRepository, times(1)).save(aBill());
    }

    @Test
    public void getAllBills(){
        Pageable pageable = PageRequest.of(1,1);
        when(billRepository.findAll(pageable)).thenReturn(aBillPage());

        Page<Bill> billPage = billService.getAllBills(pageable);

        assertEquals(aBillPage().getTotalElements(), billPage.getTotalElements());
        assertEquals(aBillPage().getTotalPages(), billPage.getTotalPages());
        assertEquals(aBillPage().getContent(), billPage.getContent());
        verify(billRepository, times(1)).findAll(pageable);
    }

    @Test
    public void getAllSort(){
        List<Sort.Order> orders = new ArrayList<>();
        Pageable pageable = PageRequest.of(1,1, Sort.by(orders));
        when(billRepository.findAll(pageable)).thenReturn(aBillPage());

        Page<Bill> billPage = billService.getAllSort(1, 1, orders);

        assertEquals(aBillPage().getTotalElements(), billPage.getTotalElements());
        assertEquals(aBillPage().getTotalPages(), billPage.getTotalPages());
        assertEquals(aBillPage().getContent(), billPage.getContent());
        verify(billRepository, times(1)).findAll(pageable);
    }

    @Test
    public void getAllSpec(){
        Pageable pageable = PageRequest.of(1,1);
        Specification<Bill> billSpecification = aSpecBill("False Street 123");
        when(billRepository.findAll(billSpecification, pageable)).thenReturn(aBillPage()) ;

        Page<Bill> billPage = billService.getAllSpec(billSpecification, pageable);

        assertEquals(aBillPage().getTotalElements(), aBillPage().getTotalElements());
        assertEquals(aBillPage().getTotalPages(), billPage.getTotalPages());
        assertEquals(aBillPage().getContent(), billPage.getContent());
        verify(billRepository, times(1)).findAll(billSpecification, pageable);
    }

    @Test
    public void getBillByIdOk(){
        try{
            when(billRepository.findById(aBill().getId())).thenReturn(Optional.of(aBill()));

            Bill bill = billService.getBillById(aBill().getId());

            assertEquals(aBill(), bill);
            verify(billRepository, times(1)). findById(bill.getId());
        }
        catch (BillNotExistsException e){
            fail(e);
        }
    }

    @Test
    public void getBillByIdNotExists(){
        when(billRepository.findById(aBill().getId())).thenReturn(Optional.empty());

        assertThrows(BillNotExistsException.class, ()-> billService.getBillById(aBill().getId()));
        verify(billRepository, times(1)).findById(aBill().getId());
    }

    @Test
    public void addClientToBillOk(){
        try {
            Bill bill = aBill();
            bill.setUserClient(aUser());
            when(billRepository.findById(aBill().getId())).thenReturn(Optional.of(aBill()));
            when(userService.getUserById(aUser().getId())).thenReturn(aUser());
            when(billRepository.save(bill)).thenReturn(bill);

            Bill bill1 = billService.addClientToBill(aBill().getId(), aUser().getId());

            assertEquals(bill.getUserClient(), bill1.getUserClient());
        }
        catch (UserNotExistsException | BillNotExistsException e){
            fail(e);
        }
    }

    @Test
    public void addClientToBillUserNotExists(){
        when(billRepository.findById(aBill().getId())).thenReturn(Optional.empty());

        assertThrows(BillNotExistsException.class, () -> billService.addClientToBill(aBill().getId(), aUser().getId()));
        verify(billRepository, times(1)).findById(aBill().getId());
    }


    @Test
    public void addAddressToBillOk() {
        try {
            Bill bill = aBill();
            bill.setAddress(aAddress());
            when(billRepository.findById(aBill().getId())).thenReturn(Optional.of(aBill()));
            when(addressService.getAddressById(aAddress().getId())).thenReturn(aAddress());
            when(billRepository.save(bill)).thenReturn(bill);

            Bill bill1 = billService.addAddressToBill(aBill().getId(), aAddress().getId());

            assertEquals(bill.getAddress(), bill1.getAddress());
        } catch (AddressNotExistsException | BillNotExistsException e) {
            fail(e);
        }
    }

    @Test
    public void addAddressToBillBillNotExists(){
        when(billRepository.findById(aBill().getId())).thenReturn(Optional.empty());

        assertThrows(BillNotExistsException.class, () -> billService.addClientToBill(aBill().getId(), aAddress().getId()));
        verify(billRepository, times(1)).findById(aBill().getId());
    }

    @Test
    public void addMeterToBillOk(){
        try {
            Bill bill = aBill();
            bill.setMeter(aMeter());
            when(billRepository.findById(aBill().getId())).thenReturn(Optional.of(aBill()));
            when(meterService.getMeterById(aMeter().getId())).thenReturn(aMeter());
            when(billRepository.save(bill)).thenReturn(bill);

            Bill bill1 = billService.addMeterToBill(aBill().getId(), aMeter().getId());

            assertEquals(bill.getMeter(), bill1.getMeter());
        } catch (MeterNotExistsException | BillNotExistsException e) {
            fail(e);
        }
    }

    @Test
    public void addMeterToBillBillNotExists(){
        when(billRepository.findById(aBill().getId())).thenReturn(Optional.empty());

        assertThrows(BillNotExistsException.class, () -> billService.addMeterToBill(aBill().getId(), aMeter().getId()));
        verify(billRepository, times(1)).findById(aBill().getId());
    }

/*    @Test
    public void deleteBillByIdOk(){
        try{
            //given
            when(billRepository.findById(aBill().getId())).thenReturn(Optional.of(aBill()));
            doNothing().when(billRepository).deleteById(aBill().getId());

            //when
            billService.deleteBillById(aBill().getId());

            //then
            when(billRepository.findById(aBill().getId())).thenReturn(Optional.empty());
            verify(billRepository, times(1)).findById(aBill().getId());
            verify(billRepository, times(1)).deleteById(aBill().getId());
        } catch (BillNotExistsException | RestrictDeleteException e) {
            fail(e);
        }
    }*/


    @Test
    public void deleteBillByIdBillNotExists(){
        when(billRepository.findById(aBill().getId())).thenReturn(Optional.empty());

        assertThrows(BillNotExistsException.class, () -> billService.deleteBillById(aBill().getId()));
        verify(billRepository, times(1)).findById(aBill().getId());
    }





}
