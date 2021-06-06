package edu.utn.TPFinal.repository;

import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.model.Meter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer>, JpaSpecificationExecutor<Measurement> {

    Page<Measurement> findAllByMeterAndDateBetween(Meter meter,Date from, Date to, Pageable pageable);
}
