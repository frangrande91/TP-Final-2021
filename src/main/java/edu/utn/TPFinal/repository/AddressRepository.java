package edu.utn.TPFinal.repository;

import edu.utn.TPFinal.model.Address;
import edu.utn.TPFinal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>, JpaSpecificationExecutor<Address> {
    Address findByIdOrAddress(Integer id, String address);

}
