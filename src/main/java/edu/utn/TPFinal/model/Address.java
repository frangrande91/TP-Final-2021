package edu.utn.TPFinal.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_address")
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_meter")
    private Meter meter;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private User userClient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rate")
    private Rate rate;

    @NotNull(message = "street should not be null")
    private String address;

    @OneToMany(mappedBy = "address", fetch = FetchType.LAZY)
    private List<Bill> billList;

}
