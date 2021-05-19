package edu.utn.TPFinal.model.Dto;

import edu.utn.TPFinal.model.Model;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MeterDto {

    private Integer id;
    private Model model;
    private String serialNumber;
    private String password;

}
