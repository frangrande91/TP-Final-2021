package edu.utn.TPFinal.service;

import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.repository.MeasurementRepository;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class MeasurementService {

    private static final String MEASUREMENT_PATH = "measurement";
    private MeasurementRepository measurementRepository;
    private MeterService meterService;


    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, MeterService meterService) {
        this.measurementRepository = measurementRepository;
        this.meterService = meterService;
    }

    public PostResponse addMeasurement(Measurement measurement) {
        Measurement m = measurementRepository.save(measurement);
        return PostResponse
                .builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(MEASUREMENT_PATH, m.getId()))
                .build();
    }

    public Page<Measurement> getAllMeasurements(Pageable pageable) {
        return measurementRepository.findAll(pageable);

    }

    public Measurement getMeasurementById(Integer id) {
        return measurementRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Measurement not found"));
    }

    public void deleteMeasurementById(Integer id) {
        measurementRepository.deleteById(id);
    }

    public void addMeterToMeasurement(Integer id, Integer idMeter) {
        Meter meter = meterService.getMeterById(idMeter);
        Measurement measurement = getMeasurementById(id);
        measurement.setMeter(meter);
        measurementRepository.save(measurement);
    }
}
