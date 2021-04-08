package edu.utn.TPFinal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Employee extends Person {

    private String numberEmployee;

    @Size(min = 5,max = 20)
    private String userName;

    @Size(min = 5,max = 20)
    private String password;

}
