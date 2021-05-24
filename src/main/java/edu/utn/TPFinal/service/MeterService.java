package edu.utn.TPFinal.service;

import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.repository.MeterRepository;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class MeterService {

    private static final String METER_PATH = "meter";
    private MeterRepository meterRepository;

    @Autowired
    public MeterService(MeterRepository meterRepository) {
        this.meterRepository = meterRepository;
    }


    public PostResponse addMeter(Meter meter) {

        Meter m = meterRepository.save(meter);
        return PostResponse
                .builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(METER_PATH, m.getId()))
                .build();
    }

    public Page<Meter> getAllMeters(Pageable pageable) {
        return meterRepository.findAll(pageable);
    }

    public Meter getMeterById(Integer id) {
        return meterRepository.findById(id).orElseThrow((() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Meter not found")));
    }


    public void deleteMeterById(Integer id) {
        meterRepository.deleteById(id);
    }
}
