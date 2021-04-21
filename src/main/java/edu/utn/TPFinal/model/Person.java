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
<<<<<<< HEAD
public abstract class Person {

=======
@Entity
public abstract class Person {

    @Id
>>>>>>> 0e9c16025a90893abec99b39a3b43a14e229eca4
    private String dni;

    private Date birthday;

<<<<<<< HEAD
    @NotBlank(message = "The first name can not be blank")
    private String firstName;

    @NotBlank(message = "The first last name can not be blank")
    private String lastName;

=======
    @NotNull(message = "The first name can not be null")
    @NotBlank(message = "The first name can not be blank")
    private String firstName;

    @NotNull(message = "The first last name can not be null")
    @NotBlank(message = "The first last name can not be blank")
    private String lastName;

    @NotNull(message = "The email last name can not be null")
>>>>>>> 0e9c16025a90893abec99b39a3b43a14e229eca4
    @NotBlank(message = "The email last name can not be blank")
    @Email(message = "This fild must a email")
    private String email;


}
