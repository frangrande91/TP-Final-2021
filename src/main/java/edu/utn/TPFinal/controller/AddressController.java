package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.exceptions.AddressNotExistsException;
import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.Responses.Response;
import edu.utn.TPFinal.service.AddressService;
import edu.utn.TPFinal.utils.EntityResponse;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    private AddressService addressService;
    private final String ADDRESS_PATH = "address";

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping(value = "/")
    public ResponseEntity addAddress(@RequestBody Address address) {
        Address addressCreated = addressService.addAddress(address);

        return ResponseEntity
                .status(HttpStatus.OK)
                .location(EntityURLBuilder.buildURL2(ADDRESS_PATH,addressCreated.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Response.builder().message("The address has been created").build());
    }

    @GetMapping
    public ResponseEntity<List<Address>> getAllAddresses(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                         @RequestParam(value = "page", defaultValue = "0") Integer page){
        Pageable pageable = PageRequest.of(page,size);
        Page<Address> pageAddress = addressService.getAllAddress(pageable);
        return EntityResponse.response(pageAddress);
    }

    @GetMapping("/sort")
    public ResponseEntity getAllSorted(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                       @RequestParam String field1, @RequestParam String field2) {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.DESC,field1));
        orders.add(new Order(Sort.Direction.DESC,field2));
        Page<Address> addressPage = addressService.getAllSort(page,size,orders);
        return EntityResponse.response(addressPage);
    }

    @GetMapping("/spec")
    public ResponseEntity getAllSpec(
            @And({
                    @Spec(path = "address", spec = Equal.class),
                    @Spec(path = "userClient", spec = Equal.class),
                    @Spec(path = "meter", spec = Equal.class)
            }) Specification<Address> newsSpecification, Pageable pageable ){
        Page<Address> addressPage = addressService.getAllSpec(newsSpecification,pageable);
        return EntityResponse.response(addressPage);
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
    public ResponseEntity addMeterToAddress(@PathVariable Integer id, @PathVariable Integer idMeter){
        addressService.addMeterToAddress(id, idMeter);
        return ResponseEntity.status(HttpStatus.OK).body(Response.builder().message("The address has been modified"));
    }
}
