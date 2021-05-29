package edu.utn.TPFinal.model.dto;

import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.model.Meter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MeasurementDto {

    private Integer id;
    private Meter meter;
    private Bill bill;
    private Double quantityKw;
    private LocalDateTime dateTime;

}
