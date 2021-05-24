package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.exceptions.AddressNotExistsException;
import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    private AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/")
    public PostResponse addAddress(@RequestBody Address address){
        return addressService.addAddress(address);
    }

    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                         @RequestParam(value = "page", defaultValue = "0") Integer page){
        Pageable pageable = PageRequest.of(page,size);
        Page<Address> pageAddress = addressService.getAllAddress(pageable);

        return response(pageAddress);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Integer id) throws AddressNotExistsException {
        Address address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }

    @DeleteMapping("/{id}")
    public void deleteAddressById(@PathVariable Integer id){
        addressService.deleteAddressById(id);
    }

    @PutMapping("/{id}/{idMeter}")
    public void addMeterToAddress(@PathVariable Integer id, @PathVariable Integer idMeter){
        addressService.addMeterToAddress(id, idMeter);
    }

    /*
    private ResponseEntity response(List list){
        return ResponseEntity.status(list.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK).body(list);
    }
     */

    private ResponseEntity<List<Address>> response(Page<Address> pageBrand){
        if(!pageBrand.getContent().isEmpty()){
            return ResponseEntity.
                    status(HttpStatus.OK).
                    header("X-Total-Count", Long.toString(pageBrand.getTotalElements())).
                    header("X-Total-Pages", Long.toString(pageBrand.getTotalPages())).
                    body(pageBrand.getContent());
        }
        else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(pageBrand.getContent());
        }
    }


}
