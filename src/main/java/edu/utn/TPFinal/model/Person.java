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
public abstract class Person {

    private String dni;

    private Date birthday;

    @NotBlank(message = "The first name can not be blank")
    private String firstName;

    @NotBlank(message = "The first last name can not be blank")
    private String lastName;

    @NotBlank(message = "The email last name can not be blank")
    @Email(message = "This fild must a email")
    private String email;


}
