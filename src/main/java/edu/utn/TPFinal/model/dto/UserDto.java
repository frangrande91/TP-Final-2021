package edu.utn.TPFinal.model.dto;

import edu.utn.TPFinal.model.TypeUser;
import lombok.Data;
import lombok.NoArgsConstructor;

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
