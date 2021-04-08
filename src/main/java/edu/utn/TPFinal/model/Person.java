package edu.utn.TPFinal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public abstract class Person {

    @Id
    private String dni;

    private Date birthday;

    @NotNull(message = "The first name can not be null")
    @NotBlank(message = "The first name can not be blank")
    private String firstName;

    @NotNull(message = "The first last name can not be null")
    @NotBlank(message = "The first last name can not be blank")
    private String lastName;

    @NotNull(message = "The email last name can not be null")
    @NotBlank(message = "The email last name can not be blank")
    @Email(message = "This fild must a email")
    private String email;


}
