package edu.utn.TPFinal.model.Dto;

import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressDto {

    private Integer id;
    private Meter meter;
    private User userClient;
    private String address;

}
