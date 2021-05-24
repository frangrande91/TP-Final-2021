package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.exceptions.UserNotExistsException;
import edu.utn.TPFinal.model.Dto.UserDto;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.model.User;
import edu.utn.TPFinal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;
    private ConversionService conversionService;

    @Autowired
    public UserController(UserService userService, ConversionService conversionService) {
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @PostMapping(value = "/")
    public PostResponse addPerson(@RequestBody User user) {
        return userService.addUser(user);
    }


    @GetMapping
    public ResponseEntity getAllUsers(@RequestParam(value = "size", defaultValue = "10") Integer size,
                                       @RequestParam(value = "page", defaultValue = "0") Integer page) {
        Pageable pageable = PageRequest.of(page,size);
        Page<User> userPage = userService.getAllUsers(pageable);
        return response(userPage);
    }
    /*
    @GetMapping("/employees")
    public ResponseEntity<List<User>> getAllEmployees() {
        List<User> employees = userService.getAllEmployees();
        return response(employees);
    }

    @GetMapping("/clients")
    public ResponseEntity<List<User>> getAllClients() {
        List<User> clients = userService.getAllClients();
        return response(clients);
    }

     */

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Integer id) throws UserNotExistsException {
        UserDto userDto = conversionService.convert(userService.getUserById(id),UserDto.class);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{username}/{password}")
    public User login(@PathVariable String username, @PathVariable String password) {
        return userService.login(username,password);
    }

    @DeleteMapping("/")
    public void deletePerson(@RequestBody User user) {
        userService.delete(user);
    }

    @DeleteMapping("/{idUser}")
    public void deletePerson(@PathVariable Integer idUser) {
        userService.deleteById(idUser);
    }

    @PutMapping("/{idClient}/addresses/{idAddress}")
    public void addAddressToClientUser(@PathVariable Integer idClient, @PathVariable Integer idAddress) {
        userService.addAddressToClientUser(idClient,idAddress);
    }

    /*
    private ResponseEntity response(List list){
        return ResponseEntity.status(list.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK).body(list);
    }

     */

    private ResponseEntity response(Page<User> pageUser){
        if(!pageUser.getContent().isEmpty()){
            return ResponseEntity.
                    status(HttpStatus.OK).
                    header("X-Total-Count", Long.toString(pageUser.getTotalElements())).
                    header("X-Total-Pages", Long.toString(pageUser.getTotalPages())).
                    body(pageUser.getContent());
        }
        else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(pageUser.getContent());
        }

    }

}
