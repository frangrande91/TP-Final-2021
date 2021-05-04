package edu.utn.TPFinal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.AccessType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User {

    @Id
    private Integer id;

    @NotBlank(message = "The first name can not be blank")
    private String firstName;

    @NotBlank(message = "The first last name can not be blank")
    private String lastName;

    @NotBlank(message = "The username can not be blank")
    private String username;

    @NotBlank(message = "The password name can not be blank")
    private String password;

    @AccessType(AccessType.Type.PROPERTY)
    public abstract TypeUser typeUser();

}
