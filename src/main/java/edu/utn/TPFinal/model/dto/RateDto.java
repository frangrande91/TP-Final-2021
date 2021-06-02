package edu.utn.TPFinal.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateDto {

    private Integer id;
    private Double value;
    private String typeRate;

}
