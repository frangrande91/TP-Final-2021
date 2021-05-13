package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.model.Dto.UserDto;
import edu.utn.TPFinal.model.Responses.PostResponse;
import edu.utn.TPFinal.service.UserService;
import edu.utn.TPFinal.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/")
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/employees")
    public List<User> getAllEmployees() {
        return userService.getAllEmployees();
    }

    @GetMapping("/clients")
    public List<User> getAllClients() {
        return userService.getAllClients();
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Integer id) {
        return conversionService.convert(userService.getById(id),UserDto.class);
    }

    @GetMapping("/{username}/{password}")
    public User login(@PathVariable String username, @PathVariable String password) {
        return userService.login(username,password);
    }

    @PostMapping(value = "/")
    public PostResponse addPerson(@RequestBody User user) {
        return userService.addUser(user);
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

}
