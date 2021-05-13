package edu.utn.TPFinal.repository;

import edu.utn.TPFinal.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends JpaRepository<Model,Integer> {

}
