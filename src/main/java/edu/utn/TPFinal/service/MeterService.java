package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exception.RestrictDeleteException;
import edu.utn.TPFinal.exception.ViolationChangeKeyAttributeException;
import edu.utn.TPFinal.exception.alreadyExists.MeterAlreadyExistsException;
import edu.utn.TPFinal.exception.notFound.MeterNotExistsException;
import edu.utn.TPFinal.exception.notFound.ModelNotExistsException;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.Model;
import edu.utn.TPFinal.repository.MeterRepository;
import edu.utn.TPFinal.repository.ModelRepository;
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
    private ModelService modelService;

    @Autowired
    public MeterService(MeterRepository meterRepository, ModelService modelService) {
        this.meterRepository = meterRepository;
        this.modelService = modelService;
    }

    public Meter addMeter(Meter meter) throws MeterAlreadyExistsException {
         if(isNull(meterRepository.findByIdOrSerialNumber(meter.getId(),meter.getSerialNumber()))) {

             return meterRepository.save(meter);
         }
         else {
             throw new MeterAlreadyExistsException("Meter already exists");
         }
    }

    public Meter updateMeter(Integer id, Meter newMeter) throws ViolationChangeKeyAttributeException, MeterNotExistsException {
        Meter currentMeter = getMeterById(id);
        if(!(currentMeter.getId().equals(newMeter.getId())) || !(currentMeter.getSerialNumber().equals(newMeter.getSerialNumber()))) {
            throw new ViolationChangeKeyAttributeException("You can not change the id or serial number");
        }
        return meterRepository.save(newMeter);
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

    public Meter getMeterBySerialNumberAndPassword(String serialNumber, String password) throws MeterNotExistsException {
        return meterRepository.findBySerialNumberAndPassword(serialNumber, password).orElseThrow(() -> new MeterNotExistsException("Meter not exists"));
    }

    public void deleteMeterById(Integer id) throws MeterNotExistsException, RestrictDeleteException {
        Meter meter = getMeterById(id);
        if(isNull(meter.getAddress())){
            meterRepository.deleteById(id);
        }
        else {
            throw new RestrictDeleteException("Can not delete this meter because it depends of another objects");
        }
    }

    public Meter addModelToMeter(Integer id, Integer idModel) throws MeterNotExistsException, ModelNotExistsException {
        Meter meter = getMeterById(id);
        Model model = modelService.getModelById(idModel);
        meter.setModel(model);
        return meterRepository.save(meter);

    }
}
