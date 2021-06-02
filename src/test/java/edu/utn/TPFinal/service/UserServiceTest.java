package edu.utn.TPFinal.service;

import edu.utn.TPFinal.exceptions.alreadyExists.RateAlreadyExists;
import edu.utn.TPFinal.exceptions.alreadyExists.UserAlreadyExists;
import edu.utn.TPFinal.model.User;
import edu.utn.TPFinal.repository.RateRepository;
import edu.utn.TPFinal.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static edu.utn.TPFinal.utils.UserTestUtils.aUser;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void addUserOk() {

       /* try {
            Mockito.when(userRepository.save(aUser())).thenReturn(aUser());
            User user = userService.addUser(aUser());
            Mockito.verify(userRepository,Mockito.times(1)).save(user);
        }
        catch (UserAlreadyExists ex) {
            ex.printStackTrace();
        }*/

    }


}
