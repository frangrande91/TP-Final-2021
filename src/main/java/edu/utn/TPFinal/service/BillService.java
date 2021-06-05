package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.notFound.*;
import edu.utn.TPFinal.model.*;
import edu.utn.TPFinal.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class BillService {

    private BillRepository billRepository;
    private MeterService meterService;
    private MeasurementService measurementService;
    private UserService userService;
    private AddressService addressService;

    @Autowired
    public BillService(BillRepository billRepository, MeterService meterService, MeasurementService measurementService, UserService personService, AddressService addressService) {
        this.billRepository = billRepository;
        this.meterService = meterService;
        this.measurementService = measurementService;
        this.userService = personService;
        this.addressService = addressService;
    }

    public Bill addBill(Bill bill) {
        return billRepository.save(bill);
    }

    public Page<Bill> getAllBills(Pageable pageable) {
        return billRepository.findAll(pageable);
    }

    public Page<Bill> getAllBillsByUserClientAndBetweenDate(Integer idClientUser, Date from, Date to, Pageable pageable) throws UserNotExistsException, ClientNotFoundException {
        User clientUser = userService.getUserById(idClientUser);
        if(!clientUser.getTypeUser().equals(TypeUser.CLIENT)) {
            throw new ClientNotFoundException(String.format("The client with id %s ",idClientUser," do not exists"));
        }
        return billRepository.findAllByUserClientAndDateBetween(clientUser ,from, to, pageable);
    }

    public Page<Bill> getAllSort(Integer page, Integer size, List<Sort.Order> orders) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(orders));
        return billRepository.findAll(pageable);
    }

    public Page<Bill> getAllSpec(Specification<Bill> billSpecification, Pageable pageable) {
        return billRepository.findAll(billSpecification, pageable);
    }

    public Bill getBillById(Integer id) throws BillNotExistsException {
        return billRepository.findById(id).orElseThrow(() -> new BillNotExistsException("Bill not exists"));
    }

    public void addClientToBill(Integer id, Integer idClient) throws UserNotExistsException, BillNotExistsException {
        Bill bill = getBillById(id);
        User userClient = userService.getUserById(idClient);
        if(userClient.getTypeUser().equals(TypeUser.CLIENT)){
            bill.setUserClient(userClient);
        }
        billRepository.save(bill);
    }

    public void addAddressToBill(Integer id, Integer idAddress) throws AddressNotExistsException, BillNotExistsException {
        Bill bill = getBillById(id);
        Address address = addressService.getAddressById(idAddress);
        bill.setAddress(address);
        billRepository.save(bill);
    }

    public void addMeterToBill(Integer id, Integer idMeter) throws MeterNotExistsException, BillNotExistsException {
        Bill bill = getBillById(id);
        Meter meter = meterService.getMeterById(idMeter);
        bill.setMeter(meter);
        billRepository.save(bill);
    }


    public void deleteBillById(Integer id) throws BillNotExistsException{
        getBillById(id);
        billRepository.deleteById(id);
    }

    public Page<Bill> getAllSortedBetweenDate(Date from, Date to, Integer page, Integer size, List<Sort.Order> orders) {
        return null;
    }
}
