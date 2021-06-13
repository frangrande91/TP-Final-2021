package edu.utn.TPFinal.repository;

import edu.utn.TPFinal.model.Meter;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeterRepository extends JpaRepository<Meter, Integer>, JpaSpecificationExecutor<Meter> {

    Meter findByIdOrSerialNumber(Integer id, String serialNumber);
    Optional<Meter> findBySerialNumberAndPassword(String serialNumber, String password);
}
