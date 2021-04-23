package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;


    @PostMapping("/")
    public void addBill(@RequestBody Bill bill){
        billService.addBill(bill);
    }

    @GetMapping()
    public List<Bill> getAllBills(){
        return billService.getAllBills();
    }

    @GetMapping("/{id}")
    public Bill getBillById(@PathVariable Integer id){
        return billService.getBillById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBillById(@PathVariable Integer id){
        billService.deleteBillById(id);
    }

    //addClientToBill

    //addAddressToBill

    @PutMapping("/{id}/{idMeter}")
    public void addMeterToBill(@PathVariable Integer id, @PathVariable Integer idMeter){
        billService.addMeterToBill(id, idMeter);
    }

    @PatchMapping("{id}/{idMeasurement}")
    public void addInitialMeasurement(@PathVariable Integer id, Integer idMeasurement){
        billService.addInitialMeasurement(id, idMeasurement);
    }

    @PatchMapping("{id}/{idMeasurement}")
    public void addFinalalMeasurement(@PathVariable Integer id, Integer idMeasurement){
        billService.addFinalMeasurement(id, idMeasurement);
    }

}
