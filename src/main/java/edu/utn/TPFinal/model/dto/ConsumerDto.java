package edu.utn.TPFinal.model.dto;

import edu.utn.TPFinal.model.TypeUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsumerDto {

    private Integer id;
    private String name;
    private String lastName;
    private String username;
    private Double consumption;

}