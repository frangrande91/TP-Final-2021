package edu.utn.TPFinal.service;
import edu.utn.TPFinal.exception.RestrictDeleteException;
import edu.utn.TPFinal.exception.ViolationChangeKeyAttributeException;
import edu.utn.TPFinal.exception.alreadyExists.AddressAlreadyExistsException;
import edu.utn.TPFinal.exception.notFound.AddressNotExistsException;
import edu.utn.TPFinal.exception.notFound.MeterNotExistsException;
import edu.utn.TPFinal.exception.notFound.RateNotExistsException;
import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.model.User;
import edu.utn.TPFinal.repository.AddressRepository;
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

    public Address addAddress(Address address) throws AddressAlreadyExistsException {
        if(isNull(addressRepository.findByIdOrAddress(address.getId(), address.getAddress()))){
            return addressRepository.save(address);
        }
        else{
            throw new AddressAlreadyExistsException("Address already exists");
        }

    }

    public Address updateAddress(Integer id, Address newAddress) throws ViolationChangeKeyAttributeException, AddressNotExistsException {
        Address currentAddress = getAddressById(id);
        if(!(currentAddress.getId().equals(newAddress.getId())) || !(currentAddress.getAddress().equals(newAddress.getAddress()))) {
            throw new ViolationChangeKeyAttributeException("You can not change the id or address");
        }
        return addressRepository.save(newAddress);
    }

    public Page<Address> getAllAddress(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    public Page<Address> getAllSpec(Specification<Address> addressSpecification, Pageable pageable) {
        return addressRepository.findAll(addressSpecification,pageable);
    }

    public Page<Address> getAllSort(Integer page, Integer size, List<Sort.Order> orders) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));
        return addressRepository.findAll(pageable);
    }

    public List<Address> getAllByUser(User userClient) {
        return addressRepository.findAllByUserClient(userClient);
    }

    public Address getAddressById(Integer id) throws AddressNotExistsException {
        return addressRepository.findById(id).orElseThrow(() -> new AddressNotExistsException("Address not exists"));
    }

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

    public void deleteAddressById(Integer id) throws AddressNotExistsException, RestrictDeleteException {
        Address address = getAddressById(id);
        if(isNull(address.getMeter()) && isNull(address.getRate())) {
            addressRepository.deleteById(id);
        } else {
            throw new RestrictDeleteException("Can not delete this address because it depends of another objects");
        }


    }

}
