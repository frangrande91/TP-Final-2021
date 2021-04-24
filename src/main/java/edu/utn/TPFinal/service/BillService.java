package edu.utn.TPFinal.service;

import edu.utn.TPFinal.model.*;
import edu.utn.TPFinal.repository.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class BillService {

    private BillRepository billRepository;
    private MeterService meterService;
    private MeasurementService measurementService;
    private PersonService personService;
    private AddressService addressService;

    @Autowired
    public BillService(BillRepository billRepository, MeterService meterService, MeasurementService measurementService, PersonService personService, AddressService addressService) {
        this.billRepository = billRepository;
        this.meterService = meterService;
        this.measurementService = measurementService;
        this.personService = personService;
        this.addressService = addressService;

    }


    public void addBill(Bill bill) {
        billRepository.save(bill);
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

    public void addClientToBill(Integer id, String idClient) {
        Bill bill = getBillById(id);
        Person person = personService.getById(idClient);
        if(person instanceof Client){
            bill.setClient((Client)person);
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

    public void addInitialMeasurement(Integer id, Integer idMeasurement) {
        Bill bill = getBillById(id);
        Measurement measurement = measurementService.getMeasurementById(idMeasurement);
        bill.setInitialMeasurement(measurement);
        billRepository.save(bill);
    }

    public void addFinalMeasurement(Integer id, Integer idMeasurement) {
        Bill bill = getBillById(id);
        Measurement measurement = measurementService.getMeasurementById(idMeasurement);
        bill.setInitialMeasurement(measurement);
        billRepository.save(bill);
    }

}
