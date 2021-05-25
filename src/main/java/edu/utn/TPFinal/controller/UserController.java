package edu.utn.TPFinal.controller;
import edu.utn.TPFinal.exceptions.UserNotExistsException;
import edu.utn.TPFinal.model.Dto.UserDto;
import edu.utn.TPFinal.model.Responses.Response;
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
    public ResponseEntity<Response> addPerson(@RequestBody User user) {
        User userCreated = userService.addUser(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .location(EntityURLBuilder.buildURL2(USER_PATH,userCreated.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Response.builder().message("The user has been created").build());
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<User> userPage = userService.getAllUsers(pageable);
        Page<UserDto> userDtoPage = userPage.map(user -> conversionService.convert(user,UserDto.class));
        return EntityResponse.response(userDtoPage);
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
        return EntityResponse.response(userDtoPage);
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
        return EntityResponse.response(userDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) throws UserNotExistsException {
        UserDto userDto = conversionService.convert(userService.getUserById(id),UserDto.class);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{username}/{password}")
    public User login(@PathVariable String username, @PathVariable String password) {
        return userService.login(username,password);
    }

    @PutMapping("/{idClient}/addresses/{idAddress}")
    public ResponseEntity<Response> addAddressToClientUser(@PathVariable Integer idClient, @PathVariable Integer idAddress) throws UserNotExistsException {
        userService.addAddressToClientUser(idClient,idAddress);
        return ResponseEntity.ok(Response.builder().message("The user has been modified").build());
    }

    /*
    @DeleteMapping("/")
    public void deletePerson(@RequestBody User user) {
        userService.delete(user);
    }

    @DeleteMapping("/{idUser}")
    public void deletePerson(@PathVariable Integer idUser) {
        userService.deleteById(idUser);
    }
     */


}
