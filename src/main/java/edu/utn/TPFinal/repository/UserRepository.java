package edu.utn.TPFinal.repository;

import edu.utn.TPFinal.model.TypeUser;
import edu.utn.TPFinal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findByUsernameAndPassword (String username, String password);
    User findByIdOrUsername(Integer id, String username);
}
