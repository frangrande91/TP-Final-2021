package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.service.PersonService;
import edu.utn.TPFinal.model.Client;
import edu.utn.TPFinal.model.Employee;
import edu.utn.TPFinal.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/")
    public List<Person> getAll() {
        return personService.getAll();
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return personService.getAllEmployees();
    }

    @GetMapping("/clients")
    public List<Client> getAllClients() {
        return personService.getAllClients();
    }

    @GetMapping("/{id}")
    public Person getById(@PathVariable String id) {
        return personService.getById(id);
    }

    @GetMapping("/{username}/{password}")
    public Person login(@PathVariable String username, @PathVariable String password) {
        return personService.login(username,password);
    }

    @PostMapping("/")
    public void addPerson(@RequestBody Person person) {
        personService.savePerson(person);
    }

    @DeleteMapping("/")
    public void deletePerson(@RequestBody Person person) {
        personService.delete(person);
    }

    @DeleteMapping("/{idPerson}")
    public void deletePerson(@PathVariable String idPerson) {
        personService.deleteById(idPerson);
    }

    @PutMapping("/{idClient}/addresses/{idAddress}")
    public void addAddressToPerson(@PathVariable String idClient,Integer idAddress) {
        personService.addAddressToClient(idClient,idAddress);
    }

}
