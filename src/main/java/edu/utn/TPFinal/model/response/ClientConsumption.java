package edu.utn.TPFinal.model.response;

import edu.utn.TPFinal.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class ClientConsumption {

    private Double consumptionKw;
    private Double consumptionMoney;
    private Date from;
    private Date to;
    private Integer quantityMeasurements;
    private UserDto clientUser;

}
