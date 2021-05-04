package edu.utn.TPFinal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "addresses")
public class Address {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull(message = "street should not be null")
    private String street;

    @NotNull(message = "number should not be null")
    private Integer number;

    private String floor;

    @OneToOne
    @JoinColumn(name = "meter_id")
    private Meter meter;


}
