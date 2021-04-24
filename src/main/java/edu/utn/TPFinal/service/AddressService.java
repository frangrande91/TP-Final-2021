package edu.utn.TPFinal.service;

import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class AddressService {

    private AddressRepository addressRepository;
    private MeterService meterService;

    @Autowired
    public AddressService(AddressRepository addressRepository, MeterService meterService) {
        this.addressRepository = addressRepository;
        this.meterService = meterService;
    }


    public void addAddress(Address address) {
        addressRepository.save(address);
    }

    public List<Address> getAllAddress() {
        return addressRepository.findAll();
    }

    public Address getAddressById(Integer id) {
        return addressRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Address not found"));
    }

    public void deleteAddressById(Integer id) {
        addressRepository.deleteById(id);
    }

    public void addMeterToAddress(Integer id, Integer idMeter) {
        Address address = getAddressById(id);
        Meter meter = meterService.getMeterById(idMeter);
        address.setMeter(meter);
        addressRepository.save(address);
    }

}
