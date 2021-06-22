package edu.utn.TPFinal.repository;

import edu.utn.TPFinal.model.Measurement;
import edu.utn.TPFinal.model.Meter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer>, JpaSpecificationExecutor<Measurement> {

   /* @Query(value = "SELECT * FROM measurements WHERE (`date` BETWEEN :from AND :to) AND (id_meter = :id_meter);", nativeQuery = true)
    List<Measurement> findAllByMeterAndDateBetweenSome(@Param("id_meter")int id_meter, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
*/
    List<Measurement> findAllByMeterAndDateBetween(Meter meter, LocalDateTime from, LocalDateTime to);
    Page<Measurement> findAllByMeterAndDateBetween(Meter meter, LocalDateTime from, LocalDateTime to, Pageable pageable);
}
