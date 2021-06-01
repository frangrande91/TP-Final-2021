package edu.utn.TPFinal.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.utn.TPFinal.model.Meter;
import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.model.TypeUser;
import edu.utn.TPFinal.model.User;
import edu.utn.TPFinal.repositories.ClassRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.SetJoin;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TestUtils {

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

    public static Specification<Meter> hasString(String searchString) {
        return (root, query, cb) -> {
            query.distinct(true);
            if (searchString != null) {
                return cb.like(root.get("meters_.serial_number"), "%" + searchString + "%");
            } else {
                return null;
            }
        };
    }

    public static Specification<Meter> hasClasses(String searchString, ClassRepository classRepository) {

        return (root, query, cb) -> {
            query.distinct(true);
            if (searchString != null) {
                List<Class> classes = classRepository.findAllByNameContainsIgnoreCase(searchString);

                if (!CollectionUtils.isEmpty(classes)) {
                    SetJoin<Meter, Class> masterClassJoin = root.joinSet("classes", JoinType.LEFT);
                    List<Predicate> predicates = new ArrayList<>();
                    predicates.add(masterClassJoin.in(new HashSet<>(classes)));
                    Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
                    return cb.or(p);
                }
            }

            return null;
        };
    }

    /*Utils Rates*/
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

    /*Utils*/
    public static String aUserJSON() {
        final Gson gson =  new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
                .setPrettyPrinting().create();
        System.out.println("");
        return gson.toJson(aUser());
    }

    public static User aUser() {
        return User.builder().id(1).firstName("Nahuel").lastName("Salomon").typeUser(TypeUser.CLIENT).username("nahuelmdp").password("1234").build();
    }

    public static Page<User> aUserPage() {
        return new PageImpl<>(List.of(aUser()));
    }


}
