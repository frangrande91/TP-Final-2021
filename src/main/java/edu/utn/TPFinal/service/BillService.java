package edu.utn.TPFinal.service;

import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.model.Meter;
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

    @Autowired
    public BillService(BillRepository billRepository, MeterService meterService, MeasurementService measurementService) {
        this.billRepository = billRepository;
        this.meterService = meterService;
        this.measurementService = measurementService;

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

    //addClientToBill

    //addAddressToBill

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
