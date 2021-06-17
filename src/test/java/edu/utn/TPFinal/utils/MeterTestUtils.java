package edu.utn.TPFinal.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.dto.MeterDto;
import edu.utn.TPFinal.utils.localdate.LocalDateDeserializer;
import edu.utn.TPFinal.utils.localdate.LocalDateSerializer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.SetJoin;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static edu.utn.TPFinal.utils.AddressTestUtils.aAddress;

public class MeterTestUtils {
    public static String aMeterJSON() {
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

    public static Page<Meter> aMeterPageWithSpecifications() {
        return new PageImpl<>(List.of(aMeter()));
    }

    public static MeterDto aMeterDto() {
        return new MeterDto();
    }

    public static Specification<Meter> specMeter(String serialNumber) {
        if(serialNumber == null) {
            return null;
        }
        else {
            return (root, criteriaQuery, criteriaBuilder) -> {
                return criteriaBuilder.like(root.get("meters.serial_number"), "%" + serialNumber + "%");
            };
        }
    }

}
