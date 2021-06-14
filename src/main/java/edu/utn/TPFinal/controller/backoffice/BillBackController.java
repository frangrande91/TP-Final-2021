package edu.utn.TPFinal.controller.backoffice;

import edu.utn.TPFinal.exception.RestrictDeleteException;
import edu.utn.TPFinal.exception.notFound.AddressNotExistsException;
import edu.utn.TPFinal.exception.notFound.BillNotExistsException;
import edu.utn.TPFinal.exception.notFound.MeterNotExistsException;
import edu.utn.TPFinal.exception.notFound.UserNotExistsException;
import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.model.dto.BillDto;
import edu.utn.TPFinal.model.response.Response;
import edu.utn.TPFinal.service.BillService;
import edu.utn.TPFinal.utils.EntityResponse;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/backoffice/bills")
public class BillBackController {

    private BillService billService;
    private ConversionService conversionService;
    private static final String BILL_PATH = "bills";

    public BillBackController(BillService billService, ConversionService conversionService) {
        this.billService = billService;
        this.conversionService = conversionService;

    }

    /**PUNTO 4*/
    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping("/addresses/{idAddress}/unpaid")
    public ResponseEntity<List<BillDto>> getAllUnpaidByAddress(@PathVariable Integer idAddress,
                                                               @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                               @RequestParam(value = "page", defaultValue = "0") Integer page) throws AddressNotExistsException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Bill> billPage = billService.getAllUnpaidByAddress(idAddress,pageable);
        Page<BillDto> billDtoPage = billPage.map(bill -> conversionService.convert(bill, BillDto.class));
        return EntityResponse.listResponse(billDtoPage);
    }

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping("/addresses/{idAddress}/unpaid/sort")
    public ResponseEntity<List<BillDto>> getAllUnpaidByAddress(@PathVariable Integer idAddress,
                                                               @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                               @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                               @RequestParam String field1, @RequestParam String field2) throws AddressNotExistsException {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,field1));
        orders.add(new Sort.Order(Sort.Direction.DESC,field2));
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        Page<Bill> billPage = billService.getAllUnpaidByAddress(idAddress,pageable);
        Page<BillDto> billDtoPage = billPage.map(bill -> conversionService.convert(bill, BillDto.class));
        return EntityResponse.listResponse(billDtoPage);
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBillById(@PathVariable Integer id) throws BillNotExistsException, RestrictDeleteException {
        billService.deleteBillById(id);
        return ResponseEntity.accepted().build();
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

    @PreAuthorize(value = "hasAuthority('EMPLOYEE')")
    @GetMapping("/users/{idClient}")
    public ResponseEntity<List<BillDto>> getAllBills( @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                      @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Bill> billPage = billService.getAllBills(pageable);
        Page<BillDto> billDtoPage = billPage.map(bill -> conversionService.convert(bill, BillDto.class));
        return EntityResponse.listResponse(billDtoPage);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<BillDto>> getAllSorted( @RequestParam(value = "size", defaultValue = "10") Integer size,
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
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillDto> getBillById(@PathVariable Integer id) throws BillNotExistsException {
        Bill bill = billService.getBillById(id);
        BillDto billDto = conversionService.convert(billService.getBillById(id), BillDto.class);
        return ResponseEntity.ok(billDto);
    }

    @PutMapping("/{id}/clients/{idClient}")
    public ResponseEntity<Response> addClientToBill(@PathVariable Integer id, @PathVariable Integer idClient) throws UserNotExistsException, BillNotExistsException {
        billService.addClientToBill(id, idClient);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Response.builder().message("The bill has been modified").build());
    }


    @PutMapping("/{id}/addresses/{idAddress}")
    public ResponseEntity<Response> addAddressToBill(@PathVariable Integer id, @PathVariable Integer idAddress) throws AddressNotExistsException, BillNotExistsException{
        billService.addAddressToBill(id, idAddress);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Response.builder().message("The bill has been modified").build());
    }

    @PutMapping("/{id}/meters/{idMeter}")
    public ResponseEntity<Response> addMeterToBill(@PathVariable Integer id, @PathVariable Integer idMeter) throws MeterNotExistsException, BillNotExistsException {
        billService.addMeterToBill(id, idMeter);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Response.builder().message("The bill has been modified").build());
    }
}
