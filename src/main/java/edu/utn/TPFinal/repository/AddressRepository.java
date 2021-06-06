package edu.utn.TPFinal.repository;

import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>, JpaSpecificationExecutor<Address> {
    List<Address> findAllByUserClient(User userClient);
    Address findByIdOrAddress(Integer id, String address);
}
