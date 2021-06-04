package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.alreadyExists.MeterAlreadyExistsException;
import edu.utn.TPFinal.exceptions.notFound.MeasurementNotExistsException;
import edu.utn.TPFinal.exceptions.notFound.MeterNotExistsException;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.repository.MeterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class MeterService {

    private static final String METER_PATH = "meter";
    private MeterRepository meterRepository;

    @Autowired
    public MeterService(MeterRepository meterRepository) {
        this.meterRepository = meterRepository;
    }

    public Meter addMeter(Meter meter) throws MeterAlreadyExistsException {
         if(isNull(meterRepository.findByIdOrSerialNumber(meter.getId(),meter.getSerialNumber()))) {
             return meterRepository.save(meter);
         }
         else {
             throw new MeterAlreadyExistsException("Meter already exists");
         }
    }

    public Page<Meter> getAllMeters(Pageable pageable) {
        return meterRepository.findAll(pageable);
    }

    public Page<Meter> getAllSpec(Specification<Meter> meterSpecifications, Pageable pageable) {
        return meterRepository.findAll(meterSpecifications,pageable);
    }

    public Page<Meter> getAllSort(Integer page, Integer size, List<Sort.Order> orders) {
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        return meterRepository.findAll(pageable);
    }

    public Meter getMeterById(Integer id) throws MeterNotExistsException {
        return meterRepository.findById(id).orElseThrow(() -> new MeterNotExistsException("Meter not exists"));
    }

    public void deleteMeterById(Integer id) throws MeterNotExistsException {
        getMeterById(id);
        meterRepository.deleteById(id);
    }

}
