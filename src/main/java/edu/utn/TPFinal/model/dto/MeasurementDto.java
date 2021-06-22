package edu.utn.TPFinal.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private BillDto bill;
    private Double quantityKw;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;
    private Double priceMeasurement;

}
