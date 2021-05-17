package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.exceptions.BillNotExistsException;
import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bill")
public class BillController {

    private BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping("/")
    public PostResponse addBill(@RequestBody Bill bill){
        return billService.addBill(bill);
    }

    @GetMapping()
    public ResponseEntity<Bill> getAllBills(){
        List<Bill> bills = billService.getAllBills();
        return response(bills);
    }

    @GetMapping("/{id}")
    public ResponseEntity getBillById(@PathVariable Integer id) throws BillNotExistsException {
        Bill bill = billService.getBillById(id);
        return ResponseEntity.ok(bill);
    }

    @DeleteMapping("/{id}")
    public void deleteBillById(@PathVariable Integer id){
        billService.deleteBillById(id);
    }

    @PutMapping("/{id}/{idClient}")
    public void addClientToBill(@PathVariable Integer id, @PathVariable Integer idClient){
        billService.addClientToBill(id, idClient);
    }

    @PutMapping("/{id}/{idAddress}")
    public void addAddressToBill(@PathVariable Integer id, @PathVariable Integer idAddress){
        billService.addAddressToBill(id, idAddress);
    }

    @PutMapping("/{id}/{idMeter}")
    public void addMeterToBill(@PathVariable Integer id, @PathVariable Integer idMeter){
        billService.addMeterToBill(id, idMeter);
    }

    private ResponseEntity response(List list){
        return ResponseEntity.status(list.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK).body(list);
    }


}
