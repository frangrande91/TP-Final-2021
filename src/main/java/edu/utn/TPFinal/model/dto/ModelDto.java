package edu.utn.TPFinal.model.dto;

import edu.utn.TPFinal.model.Brand;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ModelDto {

    private Integer id;
    private Brand brand;
    private String name;

}
