package edu.utn.TPFinal.controller.app;

import edu.utn.TPFinal.exception.AccessNotAllowedException;
import edu.utn.TPFinal.exception.notFound.*;
import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.model.dto.BillDto;
import edu.utn.TPFinal.model.dto.UserDto;
import edu.utn.TPFinal.service.BillService;
import edu.utn.TPFinal.utils.EntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static edu.utn.TPFinal.utils.Utils.checkFromTo;

@RestController
@RequestMapping("/app/bills")
public class BillAppController {

    private BillService billService;
    private ConversionService conversionService;

    @Autowired
    public BillAppController(BillService billService, ConversionService conversionService) {
        this.billService = billService;
        this.conversionService = conversionService;
    }

    /** PUNTO 2*/
    @PreAuthorize(value = "hasAuthority('EMPLOYEE') OR hasAuthority('CLIENT')")
    @GetMapping("/users/{idClient}")
    public ResponseEntity<List<BillDto>> getAllByUserClientAndBetweenDate(@PathVariable Integer idClient,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "from", defaultValue = "2020-12-05") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                                    @RequestParam(value = "to", defaultValue = "2020-01-05") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate to,
                                                    Authentication authentication) throws UserNotExistsException, ClientNotFoundException, AccessNotAllowedException {
        checkFromTo(from,to);
        UserDto userDto = (UserDto) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page, size);
        Page<Bill> billPage = billService.getAllBillsByUserClientAndBetweenDate(idClient,userDto.getId(), from, to, pageable);
        Page<BillDto> billDtoPage = billPage.map(bill -> conversionService.convert(bill, BillDto.class));
        return EntityResponse.listResponse(billDtoPage);
    }

    /*Plus punto 2*/
    @PreAuthorize(value = "hasAuthority('EMPLOYEE') OR hasAuthority('CLIENT')")
    @GetMapping("/users/{idClient}/sort")
    public ResponseEntity<List<BillDto>> getAllSortsByUserClientBetweenDate(@PathVariable Integer idClient,
                                                                                   @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                                   @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                                   @RequestParam(value = "field1") String field1, @RequestParam(value = "field2") String field2,
                                                                                   @RequestParam(value = "from", defaultValue = "2020-01-05") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate from,
                                                                                   @RequestParam(value = "to", defaultValue = "2020-12-05") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate to,
                                                                                   Authentication authentication) throws UserNotExistsException, ClientNotFoundException, AccessNotAllowedException {
        checkFromTo(from,to);
        UserDto userDto = (UserDto) authentication.getPrincipal();
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, field1));
        orders.add(new Sort.Order(Sort.Direction.DESC, field2));
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        Page<Bill> billPage = billService.getAllBillsByUserClientAndBetweenDate(idClient,userDto.getId(), from, to, pageable);
        Page<BillDto> billDtoPage = billPage.map(bill -> conversionService.convert(bill, BillDto.class));
        return EntityResponse.listResponse(billDtoPage);

    }

    /** PUNTO 3*/
    @PreAuthorize(value = "hasAuthority('EMPLOYEE') OR hasAuthority('CLIENT')")
    @GetMapping("unpaid/users/{idClient}/")
    public ResponseEntity<List<BillDto>> getAllUnpaidByUserClient(@PathVariable Integer idClient,
                                                                  @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                                  @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                  Authentication authentication) throws UserNotExistsException, ClientNotFoundException,AccessNotAllowedException {

        UserDto userDto = (UserDto) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page, size);
        Page<Bill> billPage = billService.getAllUnpaidByUserClient(userDto.getId(),idClient, pageable);
        Page<BillDto> billDtoPage = billPage.map(bill -> conversionService.convert(bill, BillDto.class));
        return EntityResponse.listResponse(billDtoPage);
    }

}
