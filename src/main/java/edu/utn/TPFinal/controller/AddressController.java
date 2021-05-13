package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;


    @PostMapping("/")
    public PostResponse addAddress(@RequestBody Address address){

        return addressService.addAddress(address);
    }

    @GetMapping
    public List<Address> getAllAddresses(){
       return addressService.getAllAddress();
    }

    @GetMapping("/{id}")
    public Address getAddressById(@PathVariable Integer id){
        return addressService.getAddressById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteAddressById(@PathVariable Integer id){
        addressService.deleteAddressById(id);
    }

    @PutMapping("/{id}/{idMeter}")
    public void addMeterToAddress(@PathVariable Integer id, @PathVariable Integer idMeter){
        addressService.addMeterToAddress(id, idMeter);
    }


}
