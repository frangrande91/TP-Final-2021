package edu.utn.TPFinal.model.dto;

import edu.utn.TPFinal.model.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeterDto {

    private Integer id;
    private ModelDto model;
    private String serialNumber;
    private String password;

}
