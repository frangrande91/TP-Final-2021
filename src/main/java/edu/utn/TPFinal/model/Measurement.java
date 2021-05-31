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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_measurement")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_meter")
    private Meter meter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bill")
    private Bill bill;

    @NotNull(message = "quantity should not be null")
    private Double quantityKw;

    @NotNull (message = "dateTime should not be null")
    private LocalDateTime dateTime;
    
    private Double priceMeasurement;

}
