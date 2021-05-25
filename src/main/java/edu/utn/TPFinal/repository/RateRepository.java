package edu.utn.TPFinal.repository;

import edu.utn.TPFinal.model.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate,Integer>, JpaSpecificationExecutor<Rate> {

}
