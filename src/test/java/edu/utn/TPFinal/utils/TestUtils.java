package edu.utn.TPFinal.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import edu.utn.TPFinal.exception.FromToInvalidException;
import static edu.utn.TPFinal.utils.Utils.checkFromTo;
import static edu.utn.TPFinal.utils.Utils.checkFromToTime;

public class TestUtils {

    @Test
    public void testFromToOk() {
        LocalDate from = LocalDate.of(2020,1,1);
        LocalDate to = LocalDate.of(2020,1,2);
        checkFromTo(from,to);
    }


    @Test
    public void testFromToFail() {
        LocalDate from = LocalDate.of(2020,1,2);
        LocalDate to = LocalDate.of(2020,1,1);
        Assertions.assertThrows(FromToInvalidException.class,()->{
            checkFromTo(from,to);
        });
    }

    @Test
    public void testFromToTimeOk() {
        LocalDateTime from = LocalDateTime.of(2020,1,1,0,0,0);
        LocalDateTime to = LocalDateTime.of(2020,1,2,0,0,0);
        checkFromToTime(from,to);
    }


    @Test
    public void testFromToTimeFail() {
        LocalDateTime from = LocalDateTime.of(2020,1,2,0,0,0);
        LocalDateTime to = LocalDateTime.of(2020,1,1,0,0,0);
        Assertions.assertThrows(FromToInvalidException.class,()->{
            checkFromToTime(from,to);
        });
    }

}
