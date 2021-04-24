package edu.utn.TPFinal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends Person {

    private Integer yearsOfAntiquity;

    @Override
    public TypePerson typePerson() {
        return TypePerson.USER;
    }
}
