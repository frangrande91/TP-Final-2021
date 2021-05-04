package edu.utn.TPFinal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "employees")
public class EmployeeUser extends User {

    private Integer yearsOfAntiquity;

    @Override
    public TypeUser typeUser() {
        return TypeUser.EMPLOYEE;
    }
}
