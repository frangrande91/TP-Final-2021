package edu.utn.TPFinal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
<<<<<<< HEAD
import java.util.List;
=======
>>>>>>> 0e9c16025a90893abec99b39a3b43a14e229eca4

@Data
@NoArgsConstructor
@AllArgsConstructor
<<<<<<< HEAD
public class Client extends Person{

    private List<Address> addresses;
=======
@Entity
public class Client extends Person{

    /*private List<Address> addresses;*/
>>>>>>> 0e9c16025a90893abec99b39a3b43a14e229eca4

}
