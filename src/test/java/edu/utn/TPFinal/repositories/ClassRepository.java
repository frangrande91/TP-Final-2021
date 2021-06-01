package edu.utn.TPFinal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ClassRepository extends JpaRepository<Class, Long>, JpaSpecificationExecutor {
    List<Class> findAllByNameContainsIgnoreCase(String searchString);
}
