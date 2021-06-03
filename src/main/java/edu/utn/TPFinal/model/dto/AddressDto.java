package edu.utn.TPFinal.model.dto;

import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDto {

    private Integer id;
    private MeterDto meter;
    private UserDto userClient;
    private String address;
    private RateDto rate;
}
