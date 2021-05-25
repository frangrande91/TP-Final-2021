package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.exceptions.BillNotExistsException;
import edu.utn.TPFinal.exceptions.MeterNotExistsException;
import edu.utn.TPFinal.exceptions.UserNotExistsException;
import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.service.BillService;
import edu.utn.TPFinal.utils.EntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Bill> getAllBills(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                            @RequestParam(value = "page", defaultValue = "0") Integer page){
        Pageable pageable = PageRequest.of(page,size);
        Page<Bill> pageBill = billService.getAllBills(pageable);
        return EntityResponse.response(pageBill);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable Integer id) throws BillNotExistsException {
        Bill bill = billService.getBillById(id);
        return ResponseEntity.ok(bill);
    }

    @DeleteMapping("/{id}")
    public void deleteBillById(@PathVariable Integer id){
        billService.deleteBillById(id);
    }

    @PutMapping("/{id}/{idClient}")
    public void addClientToBill(@PathVariable Integer id, @PathVariable Integer idClient) throws UserNotExistsException {
        billService.addClientToBill(id, idClient);
    }

    @PutMapping("/{id}/{idAddress}")
    public void addAddressToBill(@PathVariable Integer id, @PathVariable Integer idAddress){
        billService.addAddressToBill(id, idAddress);
    }

    @PutMapping("/{id}/{idMeter}")
    public void addMeterToBill(@PathVariable Integer id, @PathVariable Integer idMeter) throws MeterNotExistsException {
        billService.addMeterToBill(id, idMeter);
    }

}
