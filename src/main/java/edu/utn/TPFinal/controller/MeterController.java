package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.exceptions.MeterNotExistsException;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.service.MeterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meter")
public class MeterController {

    private MeterService meterService;

    @Autowired
    public MeterController(MeterService meterService) {
        this.meterService = meterService;
    }

    @PostMapping("/")
    public PostResponse addMeter(@RequestBody Meter meter){
        return meterService.addMeter(meter);
    }

    @GetMapping()
    public ResponseEntity getAllMeters(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Meter> meterPage = meterService.getAllMeters(pageable);
        return response(meterPage);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Meter> getMeterById(@PathVariable Integer id) throws MeterNotExistsException {
        Meter meter = meterService.getMeterById(id);
        return ResponseEntity.ok(meter);
    }

    @DeleteMapping("/{id}")
    public void deleteMeterById(@PathVariable Integer id){
        meterService.deleteMeterById(id);
    }

    /*
    private ResponseEntity response(List list){
        return ResponseEntity.status(list.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK).body(list);
    }
     */

    private ResponseEntity response(Page<Meter> meterPage){
        if(!meterPage.getContent().isEmpty()){
            return ResponseEntity.
                    status(HttpStatus.OK).
                    header("X-Total-Count", Long.toString(meterPage.getTotalElements())).
                    header("X-Total-Pages", Long.toString(meterPage.getTotalPages())).
                    body(meterPage.getContent());
        }
        else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(meterPage.getContent());
        }

    }

}