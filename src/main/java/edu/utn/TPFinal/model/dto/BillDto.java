package edu.utn.TPFinal.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Date initialMeasurement;
    private Date finalMeasurement;
    private Date date;
    private Double totalConsumption;
    private Double totalPayable;
    private Boolean payed;

}
