package edu.utn.TPFinal.utils;

import edu.utn.TPFinal.exception.AccessNotAllowedException;
import edu.utn.TPFinal.exception.FromToInvalidException;
import edu.utn.TPFinal.exception.notFound.ClientNotFoundException;
import edu.utn.TPFinal.model.TypeUser;
import edu.utn.TPFinal.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Utils {

    public static void checkFromTo(LocalDate from, LocalDate to) {
        if(from.isAfter(to)) {
            throw new FromToInvalidException();
        }
    }

    public static void checkFromToTime(LocalDateTime from, LocalDateTime to) {
        if(from.isAfter(to)) {
            throw new FromToInvalidException();
        }
    }

    public static void userPermissionCheck(User queryUser, User clientUser) throws ClientNotFoundException, AccessNotAllowedException {
        if(queryUser.getId().equals(clientUser.getId()) || queryUser.getTypeUser().equals(TypeUser.EMPLOYEE)) {
            if(!clientUser.getTypeUser().equals(TypeUser.CLIENT)) {
                throw new ClientNotFoundException(String.format("The client with id %s ",clientUser.getId()," do not exists"));
            }
        } else {
            throw new AccessNotAllowedException("You have not access to this resource");
        }
    }

}
