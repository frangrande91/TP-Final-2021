package edu.utn.TPFinal.service;

import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.repository.MeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class MeterService {

    @Autowired
    private MeterRepository meterRepository;

    public void addMeter(Meter meter) {
        meterRepository.save(meter);
    }

    public List<Meter> getAllMeters() {
        return meterRepository.findAll();
    }

    public Meter getMeterById(Integer id) {
        return meterRepository.findById(id).orElseThrow((() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Meter not found")));
    }


    public void deleteMeterById(Integer id) {
        meterRepository.deleteById(id);
    }
}
