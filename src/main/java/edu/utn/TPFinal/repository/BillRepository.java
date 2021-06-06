package edu.utn.TPFinal.repository;

import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.time.LocalDateTime;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer>, JpaSpecificationExecutor<Bill> {
    List<Bill> findAllByDateBetween(LocalDateTime from, LocalDateTime to);

}
