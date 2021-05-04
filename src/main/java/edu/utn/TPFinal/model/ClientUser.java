package edu.utn.TPFinal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "clients")
public class ClientUser extends User {

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_client")
    private List<Address> addresses;

    @Override
    public TypeUser typeUser() {
        return TypeUser.CLIENT;
    }
}
