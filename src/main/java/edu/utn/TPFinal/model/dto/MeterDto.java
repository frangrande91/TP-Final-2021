package edu.utn.TPFinal.model.dto;

import edu.utn.TPFinal.model.Model;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MeterDto {

    private Integer id;
    private ModelDto model;
    private String serialNumber;
    private String password;

}
