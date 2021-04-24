package edu.utn.TPFinal.repository;

import edu.utn.TPFinal.model.Person;
import edu.utn.TPFinal.model.Employee;
import edu.utn.TPFinal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, String> {
    List<Employee> findByUser(User user);
}
