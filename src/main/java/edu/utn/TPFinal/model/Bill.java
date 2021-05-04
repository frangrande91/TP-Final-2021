package edu.utn.TPFinal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "bills")
public class Bill {

    @Id
    @GeneratedValue
    private Integer id;

    @OneToOne
    @JoinColumn(name = "client_id")
    private ClientUser client;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne
    @JoinColumn(name = "meter_id")
    private Meter meter;

    @OneToOne
    @JoinColumn(name = "initial_measurement_id")
    private Measurement initialMeasurement;

    @OneToOne
    @JoinColumn(name = "final_measurement_id")
    private Measurement finalMeasurement;

    private Double totalConsuption;

    @NotNull(message = "typeRate should not be null")
    private TypeRate typeRate;

    private Double totalPayable;
}
