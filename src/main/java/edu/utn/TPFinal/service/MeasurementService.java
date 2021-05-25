package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.MeasurementNotExistsException;
import edu.utn.TPFinal.exceptions.MeterNotExistsException;
import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Measurement addMeasurement(Measurement measurement) {
        return measurementRepository.save(measurement);
    }

    public Page<Measurement> getAllMeasurements(Pageable pageable) {
        return measurementRepository.findAll(pageable);
    }

    public Page<Measurement> getAllSpec(Specification<Measurement> measurementSpecification, Pageable pageable) {
        return measurementRepository.findAll(measurementSpecification,pageable);
    }

    public Page<Measurement> getAllSort(Integer page, Integer size, List<Sort.Order> orders) {
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        return measurementRepository.findAll(pageable);
    }

    public Measurement getMeasurementById(Integer id) throws MeasurementNotExistsException{
        return measurementRepository.findById(id).orElseThrow(MeasurementNotExistsException::new);
    }

    public void addMeterToMeasurement(Integer id, Integer idMeter) throws MeasurementNotExistsException, MeterNotExistsException {
        Meter meter = meterService.getMeterById(idMeter);
        Measurement measurement = getMeasurementById(id);
        measurement.setMeter(meter);
        measurementRepository.save(measurement);
    }

    /*
    public void deleteMeasurementById(Integer id) {
        measurementRepository.deleteById(id);
    }

     */
}
