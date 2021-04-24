package edu.utn.TPFinal.Service;

import edu.utn.TPFinal.Exceptions.PersonNotFoundException;
import edu.utn.TPFinal.Persistence.PersonRepository;
import edu.utn.TPFinal.model.Client;
import edu.utn.TPFinal.model.Person;
import edu.utn.TPFinal.model.TypePerson;
import edu.utn.TPFinal.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void savePerson(Person person) {
        personRepository.save(person);
    }

    public List<Person> getAll() {
        return personRepository.findAll();
    }

    public List<Employee> getAllUsers() {
        return (List<Employee>) personRepository.findAll().stream().filter(p -> p.typePerson().equals(TypePerson.USER));
    }

    public List<Client> getAllClients() {
        return (List<Client>) personRepository.findAll().stream().filter(p -> p.typePerson().equals(TypePerson.CLIENT));
    }

    public Person getById(String id) {
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(String.format("There is no person with id: %s",id)));
    }

/*    public Employee getUserByUsernameAndPassword(String username, String password) {
        return getAllUsers().stream().filter(u -> (u.getUserName().equals(username) && u.getUserName().equals(password)))
    }*/

/*    public void delete(String id) {
        personRepository.
    }*/



}
