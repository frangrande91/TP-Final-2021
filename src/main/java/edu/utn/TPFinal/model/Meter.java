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
@Entity(name = "meters")
public class Meter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_meter")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_model")
    private Model model;

    @NotNull(message = "The serial Number can not be null")
    private String serialNumber;

    @NotNull(message = "The password can not be null")
    private String password;

    @OneToOne(mappedBy = "meter", fetch = FetchType.LAZY)
    private Address address;

    @OneToMany(mappedBy = "meter", fetch = FetchType.LAZY)
    private List<Measurement> measurementList;

    @OneToMany(mappedBy = "meter", fetch = FetchType.LAZY)
    private List<Bill> billList;

}
