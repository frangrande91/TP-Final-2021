package edu.utn.TPFinal.controller.backoffice;

import edu.utn.TPFinal.exceptions.ViolationChangeKeyAttributeException;
import edu.utn.TPFinal.exceptions.notFound.AddressNotExistsException;
import edu.utn.TPFinal.exceptions.notFound.MeterNotExistsException;
import edu.utn.TPFinal.exceptions.notFound.RateNotExistsException;
import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.dto.AddressDto;
import edu.utn.TPFinal.model.responses.Response;
import edu.utn.TPFinal.service.AddressService;
import edu.utn.TPFinal.utils.EntityResponse;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/backoffice/address")
public class AddressBackController {

    private AddressService addressService;
    private ConversionService conversionService;
    private static final String ADDRESS_PATH = "address";

    @Autowired
    public AddressBackController(AddressService addressService, ConversionService conversionService) {
        this.addressService = addressService;
        this.conversionService = conversionService;
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @PostMapping(value = "/")
    public ResponseEntity<Response> addAddress(@RequestBody Address address) throws SQLIntegrityConstraintViolationException {
        Address addressCreated = addressService.addAddress(address);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(EntityURLBuilder.buildURL2(ADDRESS_PATH,addressCreated.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(EntityResponse.messageResponse("The address has been created"));
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response> updateRate(@PathVariable Integer id,@RequestBody Address address) throws AddressNotExistsException, ViolationChangeKeyAttributeException {
        addressService.updateAddress(id,address);
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(EntityResponse.messageResponse("The address has been updated"));
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping("/")
    public ResponseEntity<List<AddressDto>> getAllAddresses(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                         @RequestParam(value = "page", defaultValue = "0") Integer page){
        Pageable pageable = PageRequest.of(page,size);
        Page<Address> addressPage = addressService.getAllAddress(pageable);
        Page<AddressDto> addressDtoPage = addressPage.map(address -> conversionService.convert(address,AddressDto.class));
        return EntityResponse.listResponse(addressDtoPage);
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping("/spec")
    public ResponseEntity<List<AddressDto>> getAllSpec(
            @And({
                    @Spec(path = "address", spec = Equal.class),
                    @Spec(path = "userClient", spec = Equal.class),
                    @Spec(path = "meter", spec = Equal.class)
            }) Specification<Address> newsSpecification, Pageable pageable ){
        Page<Address> addressPage = addressService.getAllSpec(newsSpecification,pageable);
        Page<AddressDto> addressDtoPage = addressPage.map(address -> conversionService.convert(address, AddressDto.class));
        return EntityResponse.listResponse(addressDtoPage);
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping("/sort")
    public ResponseEntity<List<AddressDto>> getAllSorted(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                                         @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                         @RequestParam String field1, @RequestParam String field2) {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.DESC,field1));
        orders.add(new Order(Sort.Direction.DESC,field2));
        Page<Address> addressPage = addressService.getAllSort(page,size,orders);
        Page<AddressDto> addressDtoPage = addressPage.map(address -> conversionService.convert(address,AddressDto.class));
        return EntityResponse.listResponse(addressDtoPage);
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Integer id) throws AddressNotExistsException {
        Address address = addressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @PutMapping("/{id}/{idMeter}")
    public ResponseEntity<Response> addMeterToAddress(@PathVariable Integer id, @PathVariable Integer idMeter) throws AddressNotExistsException, MeterNotExistsException {
        addressService.addMeterToAddress(id, idMeter);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(EntityResponse.messageResponse("The address has been modified"));
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @PutMapping("/{id}/{idRate}")
    public ResponseEntity<Response> addRateToAddress(@PathVariable Integer id, @PathVariable Integer idRate) throws AddressNotExistsException, RateNotExistsException {
        addressService.addRateToAddress(id, idRate);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(EntityResponse.messageResponse("The address has been modified"));
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAddressById(@PathVariable Integer id) throws AddressNotExistsException{
        addressService.deleteAddressById(id);
        return ResponseEntity.accepted().build();
    }

}
