package edu.utn.TPFinal.controller;
import edu.utn.TPFinal.exceptions.ErrorLoginException;
import edu.utn.TPFinal.exceptions.alreadyExists.UserAlreadyExists;
import edu.utn.TPFinal.exceptions.notFound.AddressNotExistsException;
import edu.utn.TPFinal.exceptions.notFound.ClientNotFoundException;
import edu.utn.TPFinal.exceptions.notFound.UserNotExistsException;
import edu.utn.TPFinal.model.dto.UserDto;
import edu.utn.TPFinal.model.responses.Response;
import edu.utn.TPFinal.model.User;
import edu.utn.TPFinal.service.UserService;
import edu.utn.TPFinal.utils.EntityResponse;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private ConversionService conversionService;
    private static final String USER_PATH = "users";

    @Autowired
    public UserController(UserService userService, ConversionService conversionService) {
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @PostMapping(value = "/")
    public ResponseEntity<Response> addUser(@RequestBody User user) throws UserAlreadyExists {
        User userCreated = userService.addUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(EntityURLBuilder.buildURL2(USER_PATH,userCreated.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(EntityResponse.messageResponse("The user has been created"));
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<User> userPage = userService.getAllUsers(pageable);
        Page<UserDto> userDtoPage = userPage.map(user -> conversionService.convert(user,UserDto.class));
        return EntityResponse.listResponse(userDtoPage);
    }

    @GetMapping("/sort")
    public ResponseEntity<List<UserDto>> getAllSorted(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page,
                                       @RequestParam String field1, @RequestParam String field2) {

        List<Order> orders = new ArrayList<>();
        orders.add(new Order(Sort.Direction.DESC,field1));
        orders.add(new Order(Sort.Direction.DESC,field2));
        Page<User> userPage = userService.getAllSort(page,size,orders);
        Page<UserDto> userDtoPage = userPage.map(user -> conversionService.convert(user,UserDto.class));
        return EntityResponse.listResponse(userDtoPage);
    }

    @GetMapping("/spec")
    public ResponseEntity<List<UserDto>> getAllSpec(
            @And({
                    @Spec(path = "firstname", spec = Equal.class),
                    @Spec(path = "lastname", spec = Equal.class),
                    @Spec(path = "typeUser", spec = Equal.class)
            }) Specification<User> userSpecification, Pageable pageable
    ) {

        Page<User> userPage = userService.getAllSpec(userSpecification,pageable);
        Page<UserDto> userDtoPage = userPage.map(user -> conversionService.convert(user,UserDto.class));
        return EntityResponse.listResponse(userDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) throws UserNotExistsException {
        UserDto userDto = conversionService.convert(userService.getUserById(id),UserDto.class);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{username}/{password}")
    public ResponseEntity<UserDto> login(@PathVariable String username, @PathVariable String password) throws ErrorLoginException {
        UserDto userDto = conversionService.convert(userService.login(username,password),UserDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);


    }

    @PutMapping("/{idClient}/addresses/{idAddress}")
    public ResponseEntity<Response> addAddressToClientUser(@PathVariable Integer idClient, @PathVariable Integer idAddress) throws UserNotExistsException, AddressNotExistsException, ClientNotFoundException {
        userService.addAddressToClientUser(idClient,idAddress);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(EntityResponse.messageResponse("The user has been modified"));
    }

    @DeleteMapping("/{idUser}")
    public ResponseEntity<Object> deleteUser(@PathVariable Integer idUser) throws UserNotExistsException {
        userService.deleteById(idUser);
        return ResponseEntity.accepted().build();
    }

}
