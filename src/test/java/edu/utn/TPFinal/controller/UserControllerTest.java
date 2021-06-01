package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static edu.utn.TPFinal.utils.UserTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = UserController.class)
@Slf4j
public class UserControllerTest extends AbstractController {

    @MockBean
    private UserService userService;
    @MockBean
    private FormattingConversionService formattingConversionService;

    @Test
    public void addUser() throws Exception {

        Mockito.when(userService.addUser(aUser())).thenReturn(aUser());

        ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                .post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(aUserJSON()))
                .andExpect(status().isCreated());

        assertEquals(HttpStatus.CREATED.value(),resultActions.andReturn().getResponse().getStatus());

    }

    @Test
    public void getAllUsers() throws Exception {

        Mockito.when(userService.getAllUsers(any())).thenReturn(aUserPage());

        final ResultActions resultActions = givenController()
                .perform(MockMvcRequestBuilders
                .get("/users/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        assertEquals(HttpStatus.OK.value(),resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void getAllSorted() throws Exception {

        Mockito.when(userService.getAllSort(any(),any(),anyList())).thenReturn(aUserPage());

        final ResultActions resultActions = givenController()
                .perform(MockMvcRequestBuilders
                        .get("/users/sort?field1=firstName&field2=lastName")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(HttpStatus.OK.value(),resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void getAllSpec() {

    }

    @Test
    public void getUserById() throws Exception {

        final ResultActions resultActions = givenController()
                .perform(MockMvcRequestBuilders
                .get("/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(HttpStatus.OK.value(),resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void login() throws Exception {

        final ResultActions resultActions = givenController()
                .perform(MockMvcRequestBuilders
                        .get("/users/jack50/12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertEquals(HttpStatus.OK.value(),resultActions.andReturn().getResponse().getStatus());
    }

    @Test
    public void addAddressToClientUser() throws Exception{

        ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
                .put("/users/1/addresses/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        assertEquals(HttpStatus.ACCEPTED.value(),resultActions.andReturn().getResponse().getStatus());

    }

    @Test
    public void deleteUserById() throws Exception {

        ResultActions resultActions = givenController().perform(MockMvcRequestBuilders
            .delete("/users/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isAccepted());

        assertEquals(HttpStatus.ACCEPTED.value(),resultActions.andReturn().getResponse().getStatus());
    }

}
