package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.AccessNotAllowedException;
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

    public Page<Bill> getAllBillsByUserClientAndBetweenDate(Integer idQueryUser, Integer idClientUser, Date from, Date to, Pageable pageable) throws UserNotExistsException, ClientNotFoundException, AccessNotAllowedException {
        User queryUser = userService.getUserById(idQueryUser);
        User clientUser = userService.getUserById(idClientUser);
        userPermissionCheck(queryUser,clientUser);
        return billRepository.findAllByUserClientAndDateBetween(clientUser ,from, to, pageable);
    }

    public Page<Bill> getAllUnpaidByUserClient(Integer idQueryUser, Integer idClientUser, Pageable pageable) throws UserNotExistsException, AccessNotAllowedException, ClientNotFoundException {
        User queryUser = userService.getUserById(idQueryUser);
        User clientUser = userService.getUserById(idClientUser);
        userPermissionCheck(queryUser,clientUser);
        return billRepository.findAllByUserClientAndPayed(clientUser,false, pageable);
    }

    public Page<Bill> getAllUnpaidByAddress(Integer idAddress, Pageable pageable) throws AddressNotExistsException {
        Address address = addressService.getAddressById(idAddress);
        return billRepository.findAllByAddressAndPayed(address,false, pageable);
    }

    private void userPermissionCheck(User queryUser,User clientUser) throws ClientNotFoundException, AccessNotAllowedException {
        if(queryUser.getId().equals(clientUser.getId()) || queryUser.getTypeUser().equals(TypeUser.EMPLOYEE)) {
            if(!clientUser.getTypeUser().equals(TypeUser.CLIENT)) {
                throw new ClientNotFoundException(String.format("The client with id %s ",clientUser.getId()," do not exists"));
            }
        } else {
            throw new AccessNotAllowedException("You have not access to this resource");
        }
    }

    public Bill addBill(Bill bill) {
        return billRepository.save(bill);
    }

    public Page<Bill> getAllBills(Pageable pageable) {
        return billRepository.findAll(pageable);
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

    public Bill addClientToBill(Integer id, Integer idClient) throws UserNotExistsException, BillNotExistsException {
        Bill bill = getBillById(id);
        User userClient = userService.getUserById(idClient);
        if(userClient.getTypeUser().equals(TypeUser.CLIENT)){
            bill.setUserClient(userClient);
        }
        return billRepository.save(bill);
    }

    public Bill addAddressToBill(Integer id, Integer idAddress) throws AddressNotExistsException, BillNotExistsException {
        Bill bill = getBillById(id);
        Address address = addressService.getAddressById(idAddress);
        bill.setAddress(address);
        return billRepository.save(bill);
    }

    public Bill addMeterToBill(Integer id, Integer idMeter) throws MeterNotExistsException, BillNotExistsException {
        Bill bill = getBillById(id);
        Meter meter = meterService.getMeterById(idMeter);
        bill.setMeter(meter);
        return billRepository.save(bill);
    }


    public void deleteBillById(Integer id) throws BillNotExistsException{
        getBillById(id);
        billRepository.deleteById(id);
    }


}
