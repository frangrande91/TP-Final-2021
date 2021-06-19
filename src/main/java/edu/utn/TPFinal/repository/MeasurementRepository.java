package edu.utn.TPFinal.repository;

import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.model.Meter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer>, JpaSpecificationExecutor<Measurement> {

    Page<Measurement> findAllByMeterAndDateBetween(Meter meter, LocalDateTime from, LocalDateTime to, Pageable pageable);
    List<Measurement> findAllByMeterAndDateBetween(Meter meter,LocalDateTime from, LocalDateTime to);
}
