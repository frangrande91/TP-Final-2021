package edu.utn.TPFinal.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillDto {

    private Integer id;
    private AddressDto address;
    private MeterDto meter;
    private UserDto userClient;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime initialMeasurement;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime finalMeasurement;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate date;
    private Double totalConsumption;
    private Double totalPayable;
    private Boolean payed;

}
