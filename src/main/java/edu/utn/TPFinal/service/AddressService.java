package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.AddressNotExistsException;
import edu.utn.TPFinal.exceptions.MeterNotExistsException;
import edu.utn.TPFinal.exceptions.RateNotExistsException;
import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
public class AddressService {

    private AddressRepository addressRepository;
    private MeterService meterService;
    private RateService rateService;

    @Autowired
    public AddressService(AddressRepository addressRepository, MeterService meterService, RateService rateService) {
        this.addressRepository = addressRepository;
        this.meterService = meterService;
        this.rateService = rateService;
    }

    public Address addAddress(Address address) throws SQLIntegrityConstraintViolationException {
        return addressRepository.save(address);
    }

    public Page<Address> getAllAddress(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    public Page<Address> getAllSpec(Specification<Address> addressSpecification, Pageable pageable) {
        return addressRepository.findAll(addressSpecification,pageable);
    }

    public Page<Address> getAllSort(Integer page, Integer size, List<Sort.Order> orders) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(orders));
        return addressRepository.findAll(pageable);
    }


    public Address getAddressById(Integer id) throws AddressNotExistsException {
        return addressRepository.findById(id).orElseThrow(AddressNotExistsException::new);
    }

    /** USAR EL RETORNO **/
    public Address addMeterToAddress(Integer id, Integer idMeter) throws MeterNotExistsException, AddressNotExistsException {
        Address address = getAddressById(id);
        Meter meter = meterService.getMeterById(idMeter);
        address.setMeter(meter);
        return addressRepository.save(address);
    }

    public Address addRateToAddress(Integer id, Integer idRate) throws RateNotExistsException, AddressNotExistsException {
        Address address = getAddressById(id);
        Rate rate = rateService.getRateById(idRate);
        address.setRate(rate);
        return addressRepository.save(address);
    }

    /*
    public void deleteAddressById(Integer id) {
        addressRepository.deleteById(id);
    }

     */




}
