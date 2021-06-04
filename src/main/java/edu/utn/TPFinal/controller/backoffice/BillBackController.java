package edu.utn.TPFinal.controller.backoffice;

import edu.utn.TPFinal.model.dto.BillDto;
import edu.utn.TPFinal.service.BillService;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("backoffice/bills")
public class BillBackController {

    private BillService billService;
    private ConversionService conversionService;

    public BillBackController(BillService billService, ConversionService conversionService) {
        this.billService = billService;
        this.conversionService = conversionService;

    }

    @GetMapping("")
    public ResponseEntity<List<BillDto>> getUnpaid() {
        return null;
    }

}
