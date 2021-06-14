package edu.utn.TPFinal.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.AccessType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Integer id;

    @NotBlank(message = "The first name can not be blank")
    @Column(name = "name")
    private String firstName;

    @NotBlank(message = "The first last name can not be blank")
    private String lastName;

    @NotBlank(message = "The username can not be blank")
    private String username;

    @NotBlank(message = "The password can not be blank")
    private String password;

    @AccessType(AccessType.Type.PROPERTY)
    private TypeUser typeUser;

    @OneToMany(mappedBy = "userClient")
    private List<Bill> billList;

    @OneToMany(mappedBy = "userClient",fetch = FetchType.EAGER)
    private List<Address> addressList;

}
