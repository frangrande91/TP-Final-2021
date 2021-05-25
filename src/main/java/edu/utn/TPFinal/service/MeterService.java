package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.MeterNotExistsException;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.repository.MeterRepository;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public Meter addMeter(Meter meter) {
        return meterRepository.save(meter);
    }

    public Page<Meter> getAllMeters(Pageable pageable) {
        return meterRepository.findAll(pageable);
    }

    public Meter getMeterById(Integer id) throws MeterNotExistsException{
        return meterRepository.findById(id).orElseThrow(MeterNotExistsException::new);
    }

    public void deleteMeterById(Integer id) {
        meterRepository.deleteById(id);
    }

    public Page<Meter> getAllSort(Integer page, Integer size, List<Sort.Order> orders) {
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        return meterRepository.findAll(pageable);
    }

    public Page<Meter> getAllSpec(Specification<Meter> meterSpecifications, Pageable pageable) {
        return meterRepository.findAll(meterSpecifications,pageable);
    }

}
