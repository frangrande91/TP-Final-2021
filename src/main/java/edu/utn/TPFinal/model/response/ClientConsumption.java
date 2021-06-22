package edu.utn.TPFinal.model.response;

import edu.utn.TPFinal.model.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class ClientConsumption {

    private Double consumptionKw;
    private Double consumptionMoney;
    private LocalDateTime from;
    private LocalDateTime to;
    private Integer quantityMeasurements;
    private UserDto clientUser;

    public String getFrom() {
        return this.from.toString();
    }

    public String getTo() {
        return this.to.toString();
    }

    public Long getConsumptionMoney() {
        return this.consumptionMoney.longValue();
    }

}
