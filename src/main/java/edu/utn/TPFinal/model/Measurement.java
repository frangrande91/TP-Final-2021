package edu.utn.TPFinal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {

    private Double quantityKw;
    private LocalDateTime dateTime;
    private Meter meter;
}
