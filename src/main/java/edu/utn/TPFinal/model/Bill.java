package edu.utn.TPFinal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill {

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

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
    @JoinColumn(name = "initial_measurement_id")
    private Measurement finalMeasurement;

    private Double totalConsuption;

    private TypeRate typeRate;

    private Double totalPayable;
}
