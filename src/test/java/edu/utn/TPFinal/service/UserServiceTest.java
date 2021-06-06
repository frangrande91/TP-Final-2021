package edu.utn.TPFinal.service;
import edu.utn.TPFinal.exceptions.ErrorLoginException;
import edu.utn.TPFinal.exceptions.alreadyExists.MeterAlreadyExistsException;
import edu.utn.TPFinal.exceptions.alreadyExists.UserAlreadyExists;
import edu.utn.TPFinal.exceptions.notFound.AddressNotExistsException;
import edu.utn.TPFinal.exceptions.notFound.ClientNotFoundException;
import edu.utn.TPFinal.exceptions.notFound.UserNotExistsException;
import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.User;
import edu.utn.TPFinal.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static edu.utn.TPFinal.utils.AddressTestUtils.aAddress;
import static edu.utn.TPFinal.utils.MeterTestUtils.*;
import static edu.utn.TPFinal.utils.MeterTestUtils.aMeter;
import static edu.utn.TPFinal.utils.RateTestUtils.aRate;
import static edu.utn.TPFinal.utils.UserTestUtils.*;
import static org.junit.jupiter.api.Assertions.*;


public class UserServiceTest {

    private static UserRepository userRepository;
    private static UserService userService;
    private static AddressService addressService;

    @BeforeAll
    public static void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        addressService = Mockito.mock(AddressService.class);
        userService = new UserService(userRepository,addressService);
    }

    @AfterEach
    public void after() {
        Mockito.reset(userRepository);
        Mockito.reset(addressService);
    }

    @Test
    public void addUserOk() {

       try {
           Mockito.when(userRepository.findByIdOrUsername(aUser().getId(),aUser().getUsername())).thenReturn(null);
           Mockito.when(userRepository.save(aUser())).thenReturn(aUser());
           User user = userService.addUser(aUser());

           assertEquals(aUser().getId(),user.getId());
           assertEquals(aUser().getFirstName(),user.getFirstName());
           assertEquals(aUser().getLastName(),user.getLastName());
           assertEquals(aUser().getUsername(),user.getUsername());
           assertEquals(aUser().getTypeUser(),user.getTypeUser());
           assertEquals(aUser().getPassword(),user.getPassword());

           Mockito.verify(userRepository,Mockito.times(1)).findByIdOrUsername(aUser().getId(),aUser().getUsername());
           Mockito.verify(userRepository,Mockito.times(1)).save(user);
        }
        catch (UserAlreadyExists ex) {
            fail(ex);
        }
    }

    @Test
    public void addUserAlreadyExists() {
        User user = aUser();
        Mockito.when(userRepository.findByIdOrUsername(user.getId(),user.getUsername())).thenReturn(user);

        assertThrows(UserAlreadyExists.class, () -> userService.addUser(user));

        Mockito.verify(userRepository, Mockito.times(1)).findByIdOrUsername(user.getId(),user.getUsername());
        Mockito.verify(userRepository, Mockito.times(0)).save(user);
    }

    @Test
    public void getAllUsers() {
        Pageable pageable = PageRequest.of(1,1);
        Mockito.when(userRepository.findAll(pageable)).thenReturn(aUserPage());
        Page<User> userPage = userService.getAllUsers(pageable);

        assertEquals(aUserPage().getTotalElements(),userPage.getTotalElements());
        assertEquals(aUserPage().getTotalPages(),userPage.getTotalPages());
        assertEquals(aUserPage().getContent().size(),userPage.getContent().size());

        Mockito.verify(userRepository,Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void getAllSort() {
        List<Sort.Order> orders = new ArrayList<>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"firstName"));
        orders.add(new Sort.Order(Sort.Direction.DESC,"lastName"));
        Pageable pageable = PageRequest.of(1,1,Sort.by(orders));

        Mockito.when(userRepository.findAll(pageable)).thenReturn(aUserPage());
        Page<User> userPage = userService.getAllSort(1,1,orders);

        assertEquals(aMeterPage().getTotalElements(),userPage.getTotalElements());
        assertEquals(aMeterPage().getTotalPages(), userPage.getTotalElements());
        assertEquals(aMeterPage().getContent().size(),userPage.getContent().size());
        assertEquals(pageable.getSort().toList().size(), orders.size());
        assertEquals(pageable.getSort().toList().get(0), orders.get(0));
        assertEquals(pageable.getSort().toList().get(1), orders.get(1));

        Mockito.verify(userRepository,Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void getAllSpec() {
        Pageable pageable = PageRequest.of(1,1);
        Specification<User> userSpecification = specUser("Nahuel");

        Mockito.when(userRepository.findAll(userSpecification,pageable)).thenReturn(aUserPage());
        Page<User> userPage = userService.getAllSpec(userSpecification,pageable);

        assertEquals(aMeterPage().getTotalElements(),userPage.getTotalElements());
        assertEquals(aMeterPage().getTotalPages(), userPage.getTotalElements());
        assertEquals(aMeterPage().getContent().size(),userPage.getContent().size());

        Mockito.verify(userRepository,Mockito.times(1)).findAll(userSpecification,pageable);
    }

    @Test
    public void getUserByIdOk()  {
        try {
            Integer id = 1234;
            Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(aUser()));
            User user = userService.getUserById(id);

            assertEquals(aUser().getId(),user.getId());
            assertEquals(aUser().getFirstName(),user.getFirstName());
            assertEquals(aUser().getLastName(),user.getLastName());
            assertEquals(aUser().getUsername(),user.getUsername());
            assertEquals(aUser().getTypeUser(),user.getTypeUser());
            assertEquals(aUser().getPassword(),user.getPassword());

            Mockito.verify(userRepository,Mockito.times(1)).findById(id);
        }
        catch (UserNotExistsException ex) {
            fail(ex);
        }
    }

    @Test
    public void getUserByIdNotExists()  {
        Integer id = 1234;
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotExistsException.class, () -> userService.getUserById(id));

        Mockito.verify(userRepository, Mockito.times(1)).findById(id);
    }

    /*@Test
    public void loginOk() {
        try {
            Integer id = 1234;
            Mockito.when(userRepository.findByUsernameAndPassword(aUser().getUsername(),aUser().getPassword())).thenReturn(aUser());
            User user = userService.login(aUser().getUsername(),aUser().getPassword());

            assertEquals(aUser().getId(),user.getId());
            assertEquals(aUser().getFirstName(),user.getFirstName());
            assertEquals(aUser().getLastName(),user.getLastName());
            assertEquals(aUser().getUsername(),user.getUsername());
            assertEquals(aUser().getTypeUser(),user.getTypeUser());
            assertEquals(aUser().getPassword(),user.getPassword());

            Mockito.verify(userRepository,Mockito.times(1)).findByUsernameAndPassword(aUser().getUsername(),aUser().getPassword());
        }
        catch (ErrorLoginException ex) {
            fail(ex);
        }
    }
*/
    @Test
    public void loginError() {
        Integer id = 1234;
        Mockito.when(userRepository.findByUsernameAndPassword(aUser().getUsername(),aUser().getPassword())).thenReturn(null);

        assertThrows(ErrorLoginException.class, () -> userService.login(aUser().getUsername(),aUser().getPassword()));

        Mockito.verify(userRepository, Mockito.times(1)).findByUsernameAndPassword(aUser().getUsername(),aUser().getPassword());
    }


    @Test
    public void addAddressToClientUserOk()  {
        try {
            Integer idUser = 1;
            Integer idAddress = 1;
            //Address address = new Address(1,aMeter(),aUser(),aRate(),"Brown 1855", new ArrayList<>());

            Mockito.when(userRepository.findById(idUser)).thenReturn(Optional.of(aUser()));
            Mockito.when(addressService.getAddressById(1)).thenReturn(aAddress());
            Mockito.when(userRepository.save(aUser())).thenReturn(aUser());

            userService.addAddressToClientUser(idUser, idAddress);

            Mockito.verify(userRepository, Mockito.times(1)).findById(idUser);
            Mockito.verify(addressService, Mockito.times(1)).getAddressById(idAddress);
            //Mockito.verify(userRepository, Mockito.times(1)).save(aUser());
        }
        catch (UserNotExistsException | ClientNotFoundException | AddressNotExistsException ex) {
            fail(ex);
        }
    }

    @Test
    public void addAddressToClientUserUserNotExist() {

            Integer idUser = 1;
            Integer idAddress = 1;

            Mockito.when(userRepository.findById(idUser)).thenReturn(Optional.empty());

            assertThrows(UserNotExistsException.class , () -> userService.addAddressToClientUser(aUser().getId(),idAddress) );

            Mockito.verify(userRepository,Mockito.times(1)).findById(idUser);
    }

    @Test
    public void addAddressToClientUserClientNotFound() {

        Integer idUser = 1;
        Integer idAddress = 1;
        //Address address = new Address(1,aMeter(),aUser(),aRate(),"Brown 1855", new ArrayList<>());

        Mockito.when(userRepository.findById(idUser)).thenReturn(Optional.of(aUserEmployee()));

        assertThrows(ClientNotFoundException.class , () -> userService.addAddressToClientUser(aUser().getId(),idAddress) );

        Mockito.verify(userRepository,Mockito.times(1)).findById(idUser);
    }

    @Test
    public void deleteById(){
        Integer id = 1234;
        try {

            Mockito.when(userRepository.findById(id)).thenReturn(Optional.of(aUser()));
            Mockito.doNothing().when(userRepository).deleteById(id);

            userService.deleteById(id);

            Mockito.verify(userRepository, Mockito.times(1)).findById(id);
            Mockito.verify(userRepository, Mockito.times(1)).deleteById(id);

        } catch (UserNotExistsException e) {
            fail(e);
        }
    }

    @Test
    public void deleteByIdNotFound(){
        Integer id = 1234;
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());
        Mockito.doNothing().when(userRepository).deleteById(id);

        Assertions.assertThrows(UserNotExistsException.class, ()-> { userService.deleteById(id); } );

        Mockito.verify(userRepository,Mockito.times(1)).findById(id);
    }

}
