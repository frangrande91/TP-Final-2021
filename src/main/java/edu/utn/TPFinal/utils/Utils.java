package edu.utn.TPFinal.utils;

import edu.utn.TPFinal.exception.FromToInvalidException;

import java.time.LocalDate;

public class Utils {

    public static void checkFromTo(LocalDate from, LocalDate to) {
        if(from.isAfter(to)) {
            throw new FromToInvalidException();
        }
    }

}
