package edu.utn.TPFinal.model.Dto;

import edu.utn.TPFinal.model.TypeUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.AccessType;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UserDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private TypeUser typeUser;

}
