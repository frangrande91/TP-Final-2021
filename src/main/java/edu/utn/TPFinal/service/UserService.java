package edu.utn.TPFinal.service;

import edu.utn.TPFinal.Exceptions.ClientNotFoundException;
import edu.utn.TPFinal.Exceptions.ErrorLoginException;
import edu.utn.TPFinal.Exceptions.UserNotFoundException;
import edu.utn.TPFinal.model.*;
import edu.utn.TPFinal.repository.UserRepository;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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

    public PostResponse addUser(User user) {
        User p = userRepository.save(user);
        return PostResponse.builder()
                .status(HttpStatus.CREATED)
                .url(EntityURLBuilder.buildURL(USER_PATH,user.getId()))
                .build();
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public List<EmployeeUser> getAllEmployees() {
        return (List<EmployeeUser>) userRepository.findAll().stream().filter(p -> p.typeUser().equals(TypeUser.EMPLOYEE));
    }

    public List<ClientUser> getAllClients() {
        return (List<ClientUser>) userRepository.findAll().stream().filter(p -> p.typeUser().equals(TypeUser.CLIENT));
    }

    public User getById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("There is no a user with id: %s",id)));
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

    public void addAddressToClientUser(Integer idClientUser,Integer id) {
        if(getById(idClientUser) instanceof ClientUser) {
            Address address = addressService.getAddressById(id);
            ClientUser client = (ClientUser) getById(idClientUser);
            client.getAddresses().add(address);
            userRepository.save(client);
        }
        else {
            throw new ClientNotFoundException(String.format("The client with id %s ",idClientUser," do not exists"));
        }
    }


}
