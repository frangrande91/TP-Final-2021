package edu.utn.TPFinal.service;

import edu.utn.TPFinal.model.Address;

import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.repository.AddressRepository;
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
public class AddressService {

    private static final String ADDRESS_PATH = "address";
    private AddressRepository addressRepository;
    private MeterService meterService;

    @Autowired
    public AddressService(AddressRepository addressRepository, MeterService meterService) {
        this.addressRepository = addressRepository;
        this.meterService = meterService;
    }

    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }

    public Page<Address> getAllAddress(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    public Address getAddressById(Integer id) {
        return addressRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Address not found"));
    }

    public void deleteAddressById(Integer id) {
        addressRepository.deleteById(id);
    }

    public Address addMeterToAddress(Integer id, Integer idMeter) {
        Address address = getAddressById(id);
        Meter meter = meterService.getMeterById(idMeter);
        address.setMeter(meter);
        return addressRepository.save(address);
    }

    public Page<Address> getAllSpec(Specification<Address> addressSpecification, Pageable pageable) {
        return addressRepository.findAll(addressSpecification,pageable);
    }

    public Page<Address> getAllSort(Integer page, Integer size, List<Sort.Order> orders) {
        Pageable pageable = PageRequest.of(page,size, Sort.by(orders));
        return addressRepository.findAll(pageable);
    }
}
