package edu.utn.TPFinal.service;

import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class MeasurementService {

    @Autowired
    private MeasurementRepository measurementRepository;

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
}
