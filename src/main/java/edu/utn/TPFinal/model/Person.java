package edu.utn.TPFinal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.AccessType;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persons")
public abstract class Person {

    @Id
    private String dni;

    private Date birthday;

    @NotBlank(message = "The first name can not be blank")
    private String firstName;

    @NotBlank(message = "The first last name can not be blank")
    private String lastName;

    @OneToOne()
    private User user;

    @AccessType(AccessType.Type.PROPERTY)
    public abstract TypePerson typePerson();

}
