package edu.utn.TPFinal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meter {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull(message = "brand should not be null")
    private String brand;

    @NotNull(message = "model should not be null")
    private String model;
}
