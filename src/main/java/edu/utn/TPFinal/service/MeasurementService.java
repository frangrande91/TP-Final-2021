package edu.utn.TPFinal.service;

import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class MeasurementService {

    private MeasurementRepository measurementRepository;
    private MeterService meterService;


    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, MeterService meterService) {
        this.measurementRepository = measurementRepository;
        this.meterService = meterService;
    }

    public void addMeasurement(Measurement measurement) {
        measurementRepository.save(measurement);
    }


    public List<Measurement> getAllMeasuremets() {
        return measurementRepository.findAll();
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
