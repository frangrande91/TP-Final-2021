package edu.utn.TPFinal.Service;

import edu.utn.TPFinal.Exceptions.ClientNotFoundException;
import edu.utn.TPFinal.Exceptions.ErrorLoginException;
import edu.utn.TPFinal.Exceptions.PersonNotFoundException;
import edu.utn.TPFinal.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import edu.utn.TPFinal.repository.PersonRepository;
import java.util.List;
import edu.utn.TPFinal.service.AddressService;
import static java.util.Objects.isNull;

@Service
public class PersonService {

    PersonRepository personRepository;
    AddressService addressService;

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

    public List<Employee> getAllEmployees() {
        return (List<Employee>) personRepository.findAll().stream().filter(p -> p.typePerson().equals(TypePerson.EMPLOYEE));
    }

    public List<Client> getAllClients() {
        return (List<Client>) personRepository.findAll().stream().filter(p -> p.typePerson().equals(TypePerson.CLIENT));
    }

    public Person getById(String id) {
        return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(String.format("There is no a person with id: %s",id)));
    }

    public Person login(String username, String password) {
        return personRepository.findByUser(new User(username,password)).orElseThrow(() -> new ErrorLoginException("The username and/or password are incorrect"));
    }

    public void delete(Person person) {
        personRepository.delete(person);
    }

    public void deleteById(String id) {
        personRepository.deleteById(id);
    }

    public void addAddressToClient(String idClient,Integer id) {
        if(getById(idClient) instanceof Client) {
            Address address = addressService.getAddressById(id);
            Client client = (Client) getById(idClient);
            client.getAddresses().add(address);
        }
        else {
            throw new ClientNotFoundException(String.format("The client with id %s",idClient," do not exists"));
        }
    }


}
