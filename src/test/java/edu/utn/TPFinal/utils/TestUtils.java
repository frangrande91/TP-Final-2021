package edu.utn.TPFinal.utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.Model;
import edu.utn.TPFinal.model.dto.MeterDto;
import org.springframework.data.domain.PageImpl;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public class TestUtils {

    public static  String aMeterJSON() {
        final Gson gson =  new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .setPrettyPrinting().create();
        System.out.println("");
        return gson.toJson(aMeter());

    }

    public static Meter aMeter() {
        return Meter.builder().id(1).serialNumber(null).password("1235").build();
    }

    public static Page<Meter> aMeterPage() {
        return new PageImpl<>(List.of(aMeter()));
    }

    public static Specification<Meter> specMeter(String serialNumber) {
        if(serialNumber == null) {
            return null;
        }
        else {
            return (root, criteriaQuery, criteriaBuilder) -> {
                System.out.println("SERIAL NUMBER: "+criteriaBuilder.like(root.get("meters_.serial_number"), "%" + serialNumber + "%"));
                return criteriaBuilder.like(root.get("meters_.serial_number"), "%" + serialNumber + "%");/*criteriaBuilder.like(root.as( ),"%" + serialNumber + "%");*/
            };
        }
    }



}
