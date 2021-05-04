package edu.utn.TPFinal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "measurements")
public class Measurement {

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull(message = "quantity should not be null")
    private Double quantityKw;

    @NotNull (message = "dateTime should not be null")
    private LocalDateTime dateTime;

    @OneToOne
    @JoinColumn(name = "meter_id")
    private Meter meter;
}
