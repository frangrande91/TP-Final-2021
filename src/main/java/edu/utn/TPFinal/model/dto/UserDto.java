package edu.utn.TPFinal.model.dto;

import edu.utn.TPFinal.model.TypeUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    private TypeUser typeUser;

}
