package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.exceptions.notFound.*;
import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.model.TypeUser;
import edu.utn.TPFinal.model.dto.BillDto;
import edu.utn.TPFinal.model.dto.UserDto;
import edu.utn.TPFinal.model.responses.Response;
import edu.utn.TPFinal.service.BillService;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/bills")
public class BillController {

    private BillService billService;
    private ConversionService conversionService;
    private static final String BILL_PATH = "bills";

    @Autowired
    public BillController(BillService billService, ConversionService conversionService) {
        this.billService = billService;
        this.conversionService = conversionService;
    }


    @PostMapping("/")
    public ResponseEntity<Response> addBill(@RequestBody Bill bill){
        Bill billCreated = billService.addBill(bill);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(EntityURLBuilder.buildURL2(BILL_PATH,billCreated.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Response.builder().message("The bill has been created").build());
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE') OR hasAuthority('CLIENT')")
    @GetMapping("/users/{idClient}")
    public ResponseEntity<List<BillDto>> getAllBillsByUserClientAndBetweenDate(@PathVariable Integer idClient,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "from", defaultValue = "2020-12-05") @DateTimeFormat(pattern = "yyyy-MM-dd")Date from,
                                                    @RequestParam(value = "to", defaultValue = "2020-01-05") @DateTimeFormat(pattern = "yyyy-MM-dd")Date to,
                                                    Authentication authentication) throws UserNotExistsException, ClientNotFoundException {

        UserDto userDto = (UserDto) authentication.getPrincipal();

            if(userDto.getId().equals(idClient) || userDto.getTypeUser().equals(TypeUser.EMPLOYEE)) {
                Pageable pageable = PageRequest.of(page, size);
                Page<Bill> billPage = billService.getAllBillsByUserClientAndBetweenDate(idClient, from, to, pageable);
                Page<BillDto> billDtoPage = billPage.map(bill -> conversionService.convert(bill, BillDto.class));
                return EntityResponse.listResponse(billDtoPage);
            }
            else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE') OR hasAuthority('CLIENT')")
    @GetMapping("/users/{idClient}/sort")
    public ResponseEntity<List<BillDto>> getAllSortBillsByUserClientAndBetweenDate(@PathVariable Integer idClient,
                                                                                   @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                                   @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                                   @RequestParam(value = "field1") String field1, @RequestParam(value = "field2") String field2,
                                                                                   @RequestParam(value = "from", defaultValue = "2020-12-05") @DateTimeFormat(pattern = "yyyy-MM-dd")Date from,
                                                                                   @RequestParam(value = "to", defaultValue = "2020-01-05") @DateTimeFormat(pattern = "yyyy-MM-dd")Date to,
                                                                                   Authentication authentication) throws UserNotExistsException, ClientNotFoundException {
        UserDto userDto = (UserDto) authentication.getPrincipal();

        if(userDto.getId().equals(idClient) || userDto.getTypeUser().equals(TypeUser.EMPLOYEE)) {
            List<Sort.Order> orders = new ArrayList<>();
            orders.add(new Sort.Order(Sort.Direction.DESC, field1));
            orders.add(new Sort.Order(Sort.Direction.DESC, field2));
            Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
            Page<Bill> billPage = billService.getAllBillsByUserClientAndBetweenDate(idClient, from, to, pageable);
            Page<BillDto> billDtoPage = billPage.map(bill -> conversionService.convert(bill, BillDto.class));
            return EntityResponse.listResponse(billDtoPage);
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


    /*@GetMapping("/sort")
    public ResponseEntity<List<BillDto>> getAllSorted( @RequestParam(value = "from", defaultValue = "2020-12-05") @DateTimeFormat(pattern = "yyyy-MM-dd")Date from,
                                                       @RequestParam(value = "to", defaultValue = "2020-01-05") @DateTimeFormat(pattern = "yyyy-MM-dd")Date to,
                                                       @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                       @RequestParam String field1, @RequestParam String field2) {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,field1));
        orders.add(new Sort.Order(Sort.Direction.DESC,field2));
        Page<Bill> billPage = billService.getAllSort(page,size,orders);
        Page<BillDto> billDtoPage = billPage.map(bill -> conversionService.convert(bill,BillDto.class));
        return EntityResponse.listResponse(billDtoPage);
    }

    @GetMapping("/spec")
    public ResponseEntity<List<BillDto>> getAllSpec(
            @And({
                    @Spec(path = "address", spec = Equal.class),
                    @Spec(path = "meter", spec = Equal.class),
                    @Spec(path = "userClient", spec = Equal.class)
            }) Specification<Bill> newsSpecification, Pageable pageable ){
        Page<Bill> billPage = billService.getAllSpec(newsSpecification,pageable);
        Page<BillDto> billDtoPage = billPage.map(bill -> conversionService.convert(bill, BillDto.class));
        return EntityResponse.listResponse(billDtoPage);
    }*/


    @GetMapping("/{id}")
    public ResponseEntity<BillDto> getBillById(@PathVariable Integer id) throws BillNotExistsException {
        Bill bill = billService.getBillById(id);
        BillDto billDto = conversionService.convert(billService.getBillById(id), BillDto.class);
        return ResponseEntity.ok(billDto);
    }

    @PutMapping("/{id}/{idClient}")
    public ResponseEntity<Response> addClientToBill(@PathVariable Integer id, @PathVariable Integer idClient) throws UserNotExistsException, BillNotExistsException {
        billService.addClientToBill(id, idClient);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Response.builder().message("The bill has been modified").build());
    }


    @PutMapping("/{id}/{idAddress}")
    public ResponseEntity<Response> addAddressToBill(@PathVariable Integer id, @PathVariable Integer idAddress) throws AddressNotExistsException, BillNotExistsException{
        billService.addAddressToBill(id, idAddress);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Response.builder().message("The bill has been modified").build());
    }

    @PutMapping("/{id}/{idMeter}")
    public ResponseEntity<Response> addMeterToBill(@PathVariable Integer id, @PathVariable Integer idMeter) throws MeterNotExistsException, BillNotExistsException {
        billService.addMeterToBill(id, idMeter);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Response.builder().message("The bill has been modified").build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBillById(@PathVariable Integer id) throws BillNotExistsException{
        billService.deleteBillById(id);
        return ResponseEntity.accepted().build();
    }

}
