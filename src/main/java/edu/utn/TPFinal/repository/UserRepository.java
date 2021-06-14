package edu.utn.TPFinal.repository;

import edu.utn.TPFinal.model.User;
import edu.utn.TPFinal.model.projection.ConsumerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findByUsernameAndPassword (String username, String password);
    User findByIdOrUsername(Integer id, String username);

    @Query(value =
            "SELECT max_consumption.id_user AS id, \n" +
                    "       max_consumption.name,\n" +
                    "       max_consumption.last_name AS lastname, \n" +
                    "       max_consumption.username, \n" +
                    "       (SUM(max_consumption.consumption) - SUM(IFNULL(min_consumption.minimo,0))) AS consumption \n" +
                    "FROM \t\n" +
                    "\t(SELECT m.id_meter, u.id_user, u.name, u.last_name, u.username, MAX(me.quantity_kw) AS consumption\n" +
                    "\tFROM users AS u\n" +
                    "\tJOIN addresses AS a\n" +

                    "\tON u.id_user = a.id_user\n" +
                    "\tJOIN meters AS m\n" +
                    "\tON a.id_meter = m.id_meter\n" +
                    "\tJOIN measurements AS me\n" +
                    "\tON m.id_meter = me.id_meter AND (me.date BETWEEN  ?1 AND  ?2)\n" +
                    "\tGROUP BY m.id_meter, u.id_user, u.name, u.last_name, u.username) AS max_consumption\t\n" +
                    "LEFT JOIN\n" +
                    "\t(SELECT m.id_meter,IFNULL(MAX(me.quantity_kw), 0) AS minimo\n" +
                    "\tFROM users AS u\n" +
                    "\tJOIN addresses AS a\n" +
                    "\tON u.id_user = a.id_user\n" +
                    "\tJOIN meters AS m\n" +
                    "\tON a.id_meter = m.id_meter\n" +
                    "\tJOIN measurements AS me\n" +
                    "\tON m.id_meter = me.id_meter AND (me.date < ?1)    \n" +
                    "\tGROUP BY m.id_meter) AS min_consumption\n" +
                    "ON max_consumption.id_meter = min_consumption.id_meter\n" +
                    "JOIN meters m\n" +
                    "ON max_consumption.id_meter = m.id_meter\n" +
                    "GROUP BY max_consumption.id_user\t\t\t\t\t\n" +
                    "ORDER BY consumption DESC LIMIT 10\n" +
                    ";", nativeQuery = true)
    List<ConsumerProjection> getTop10MoreConsumers(LocalDate from, LocalDate to);


}
