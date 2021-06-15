package edu.utn.TPFinal.model.dto;

import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.model.Meter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeasurementDto {

    private Integer id;
    private MeterDto meter;
    private Bill bill;
    private Double quantityKw;
    private LocalDate date;
    private Double priceMeasurement;

}
