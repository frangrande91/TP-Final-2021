package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exception.RestrictDeleteException;
import edu.utn.TPFinal.exception.alreadyExists.UserAlreadyExists;
import edu.utn.TPFinal.exception.notFound.AddressNotExistsException;
import edu.utn.TPFinal.exception.notFound.ClientNotFoundException;
import edu.utn.TPFinal.exception.notFound.UserNotExistsException;
import edu.utn.TPFinal.model.*;
import edu.utn.TPFinal.model.projection.ConsumerProjection;
import edu.utn.TPFinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class UserService {

    private final String USER_PATH = "user";
    private UserRepository userRepository;
    private AddressService addressService;

    @Autowired
    public UserService(UserRepository userRepository, AddressService addressService ) {
        this.userRepository = userRepository;
        this.addressService = addressService;
    }

    public User addUser(User user) throws UserAlreadyExists {
        if(isNull(userRepository.findByIdOrUsername(user.getId(),user.getUsername()))) {
            return userRepository.save(user);
        }
        else {
            throw new UserAlreadyExists("User already exists");
        }
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Page<User> getAllSort(Integer page, Integer size, List<Sort.Order> orders) {
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        return userRepository.findAll(pageable);
    }

    public Page<User> getAllSpec(Specification<User> userSpecification, Pageable pageable) {
        return userRepository.findAll(userSpecification,pageable);
    }

    public User getUserById(Integer id) throws UserNotExistsException{
        return userRepository.findById(id).orElseThrow(() -> new UserNotExistsException("Rate not exists"));
    }

    public User login(String username, String password) {
        return userRepository.findByUsernameAndPassword(username,password);
    }

    public User addAddressToClientUser(Integer idClientUser,Integer id) throws UserNotExistsException, ClientNotFoundException, AddressNotExistsException {

        User clientUser = getUserById(idClientUser);
        Address address = addressService.getAddressById(id);

        if(clientUser.getTypeUser().equals(TypeUser.CLIENT)) {
            clientUser.getAddressList().add(address);
            return userRepository.save(clientUser);
        }
        else {
            throw new ClientNotFoundException(String.format("The client with id %s ",idClientUser," do not exists"));
        }
    }

    public Boolean containsMeter(User user, Meter meter) {
        List<Address> addressList = user.getAddressList();
        
        for(Address address : addressList) {
            if(address.getMeter().getId().equals(meter.getId())) {
                return true;
            }
        }
        return false;
    }

    public List<ConsumerProjection> getTop10MoreConsumers(LocalDate from, LocalDate to) {
        return userRepository.getTop10MoreConsumers(from, to);
    }

    public void deleteById(Integer id) throws UserNotExistsException, RestrictDeleteException {
        User user = getUserById(id);
        if(user.getBillList().isEmpty()){
            userRepository.deleteById(id);
        }
        else{
            throw new RestrictDeleteException("Can not delete this user because it depends of another objects");

        }

    }
}
