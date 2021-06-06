package edu.utn.TPFinal.repository;
import edu.utn.TPFinal.model.Bill;
import edu.utn.TPFinal.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer>, JpaSpecificationExecutor<Bill> {

    Page<Bill> findAllByUserClientAndDateBetween(User userClient, Date from, Date to, Pageable pageable);
    Page<Bill> findAllByUserClientAndPayed(User userClient, Boolean payed, Pageable pageable);

}
