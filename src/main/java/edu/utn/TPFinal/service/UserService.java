package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.alreadyExists.UserAlreadyExists;
import edu.utn.TPFinal.exceptions.notFound.AddressNotExistsException;
import edu.utn.TPFinal.exceptions.ErrorLoginException;
import edu.utn.TPFinal.exceptions.notFound.ClientNotFoundException;
import edu.utn.TPFinal.exceptions.notFound.UserNotExistsException;
import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.model.TypeUser;
import edu.utn.TPFinal.model.User;
import edu.utn.TPFinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

/*    public List<Bill> getAllBills(Integer idClientUser) throws UserNotExistsException, ClientNotFoundException {
        User clientUser = getUserById(idClientUser);
        if(!clientUser.getTypeUser().equals(TypeUser.CLIENT)) {
            throw new ClientNotFoundException(String.format("The client with id %s ",idClientUser," do not exists"));
        }
        return clientUser.getBillList();
    }*/

    public void deleteById(Integer id) throws UserNotExistsException{
        getUserById(id);
        userRepository.deleteById(id);
    }
}
