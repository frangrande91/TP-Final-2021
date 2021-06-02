package edu.utn.TPFinal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bill")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_address")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_meter")
    private Meter meter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User userClient;

    @OneToMany(mappedBy = "bill", fetch = FetchType.LAZY)
    private List<Measurement> measurementList;

    private LocalDateTime initialMeasurement;

    private LocalDateTime finalMeasurement;

    private LocalDateTime date;

    private LocalDateTime expiration;
    
    private Double totalConsumption;

    private Double totalPayable;



}
