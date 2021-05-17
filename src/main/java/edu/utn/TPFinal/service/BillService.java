package edu.utn.TPFinal.service;

import edu.utn.TPFinal.model.*;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.repository.BillRepository;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class BillService {

    private static final String BILL_PATH = "bill";
    private BillRepository billRepository;
    private MeterService meterService;
    private MeasurementService measurementService;
    private UserService personService;
    private AddressService addressService;

    @Autowired
    public BillService(BillRepository billRepository, MeterService meterService, MeasurementService measurementService, UserService personService, AddressService addressService) {
        this.billRepository = billRepository;
        this.meterService = meterService;
        this.measurementService = measurementService;
        this.personService = personService;
        this.addressService = addressService;

    }


    public PostResponse addBill(Bill bill) {
        Bill b = billRepository.save(bill);
        return PostResponse
                .builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(BILL_PATH, b.getId()))
                .build();
    }

    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    public Bill getBillById(Integer id) {
        return billRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Bill not found"));
    }

    public void deleteBillById(Integer id) {
        billRepository.deleteById(id);
    }

    public void addClientToBill(Integer id, Integer idClient) {
        Bill bill = getBillById(id);
        User userClient = personService.getById(idClient);
        if(userClient.getTypeUser().equals(TypeUser.CLIENT)){
            bill.setUserClient(userClient);
        }
        billRepository.save(bill);
    }

    public void addAddressToBill(Integer id, Integer idAddress) {
        Bill bill = getBillById(id);
        Address address = addressService.getAddressById(idAddress);
        bill.setAddress(address);
        billRepository.save(bill);
    }

    public void addMeterToBill(Integer id, Integer idMeter) {
        Bill bill = getBillById(id);
        Meter meter = meterService.getMeterById(idMeter);
        bill.setMeter(meter);
        billRepository.save(bill);
    }

}
