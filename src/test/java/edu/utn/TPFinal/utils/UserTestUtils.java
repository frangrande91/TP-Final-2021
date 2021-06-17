package edu.utn.TPFinal.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.utn.TPFinal.model.TypeUser;
import edu.utn.TPFinal.model.User;
import edu.utn.TPFinal.model.dto.UserDto;
import edu.utn.TPFinal.model.projection.ConsumerProjection;
import edu.utn.TPFinal.utils.localdate.LocalDateDeserializer;
import edu.utn.TPFinal.utils.localdate.LocalDateSerializer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static edu.utn.TPFinal.utils.AddressTestUtils.aAddress;

public class UserTestUtils {

    public static String aUserJSON() {
        final Gson gson =  new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .setPrettyPrinting().create();
        System.out.println("");
        return gson.toJson(aUser());
    }

    public static UserDto aUserDto() {
        return new UserDto(1,"Nahuel","Salomon","nahuelmdp",TypeUser.CLIENT);
    }

    public static User aUser() {
        return User.builder().id(1).firstName("Nahuel").lastName("Salomon").typeUser(TypeUser.CLIENT).username("nahuelmdp").password("1234").addressList(List.of(aAddress())).build();
    }

    public static User aUserEmployee(){
        return User.builder().id(1).firstName("Nahuel").lastName("Salomon").typeUser(TypeUser.EMPLOYEE).username("nahuelmdp").password("1234").addressList(new ArrayList<>()).build();
    }

    public static Page<User> aUserPage() {
        return new PageImpl<>(List.of(aUser()));
    }

    public static  Specification<User> specUser(String value) {
        if(value == null) {
            return null;
        }
        else {
            return (root, criteriaQuery, criteriaBuilder) -> {
                return criteriaBuilder.like(root.get("users.first_name"), "%" + value + "%");
            };
        }
    }


}
