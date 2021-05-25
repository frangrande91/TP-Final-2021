package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.ClientNotFoundException;
import edu.utn.TPFinal.exceptions.ErrorLoginException;
import edu.utn.TPFinal.exceptions.UserNotExistsException;
import edu.utn.TPFinal.exceptions.UserNotFoundException;
import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.model.TypeUser;
import edu.utn.TPFinal.model.User;
import edu.utn.TPFinal.repository.UserRepository;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final String USER_PATH = "user";
    UserRepository userRepository;
    AddressService addressService;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public List<User> getAllEmployees() {
        return userRepository.findAllByTypeUser(TypeUser.EMPLOYEE);
    }

    public List<User> getAllClients() {
        return userRepository.findAllByTypeUser(TypeUser.CLIENT);
    }

    public User getUserById(Integer id) throws UserNotExistsException{
        return userRepository.findById(id).orElseThrow(UserNotExistsException::new);
    }

    public User login(String username, String password) {
        return Optional.ofNullable(userRepository.findByUsernameAndPassword(username,password)).orElseThrow(() -> new ErrorLoginException("The username and/or password are incorrect"));
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public void addAddressToClientUser(Integer idClientUser,Integer id) throws UserNotExistsException{

        User clientUser = getUserById(idClientUser);

        if(clientUser.getTypeUser().equals(TypeUser.CLIENT)) {
            Address address = addressService.getAddressById(id);
            clientUser.getAddressList().add(address);
            userRepository.save(clientUser);
        }
        else {
            throw new ClientNotFoundException(String.format("The client with id %s ",idClientUser," do not exists"));
        }
    }


    public Page<User> getAllSort(Integer page, Integer size, List<Sort.Order> orders) {
        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        return userRepository.findAll(pageable);
    }

    public Page<User> getAllSpec(Specification<User> userSpecification, Pageable pageable) {
        return userRepository.findAll(userSpecification,pageable);
    }
}
