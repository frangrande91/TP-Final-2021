package edu.utn.TPFinal.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.utils.localdate.LocalDateDeserializer;
import edu.utn.TPFinal.utils.localdate.LocalDateSerializer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class RateTestUtils {

    public static String aRateJSON() {
        final Gson gson =  new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .setPrettyPrinting().create();
        System.out.println("");
        return gson.toJson(aRate());
    }

    public static Rate aRate() {
        return Rate.builder().id(1).typeRate("A").value(300.00).build();
    }

    public static Page<Rate> aRatePage() {
        return new PageImpl<>(List.of(aRate()));
    }

    public static Specification<Rate> specRates(Double value) {
        if(value == null) {
            return null;
        }
        else {
            return (root, criteriaQuery, criteriaBuilder) -> {
                return criteriaBuilder.like(root.get("rates.value"), "%" + value + "%");
            };
        }
    }

}
