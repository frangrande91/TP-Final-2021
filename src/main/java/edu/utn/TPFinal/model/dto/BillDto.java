package edu.utn.TPFinal.model.dto;

import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BillDto {

    private Integer id;
    private Address address;
    private Meter meter;
    private User userClient;
    private LocalDateTime initialMeasurement;
    private LocalDateTime finalMeasurement;
    private Double totalConsumption;
    private Double totalPayable;

}
