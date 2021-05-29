package edu.utn.TPFinal.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RateDto {

    private Integer id;
    private Double value;
    private String typeRate;

}
