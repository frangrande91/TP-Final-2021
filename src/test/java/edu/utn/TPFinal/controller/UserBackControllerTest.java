package edu.utn.TPFinal.controller;

import edu.utn.TPFinal.controller.backoffice.UserBackController;
import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.model.User;
import edu.utn.TPFinal.model.dto.UserDto;
import edu.utn.TPFinal.model.responses.Response;
import edu.utn.TPFinal.service.UserService;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import static edu.utn.TPFinal.utils.RateTestUtils.*;
import static edu.utn.TPFinal.utils.RateTestUtils.aRate;
import static edu.utn.TPFinal.utils.UserTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.*;

public class UserBackControllerTest {


    private static UserService userService;
    private static ConversionService conversionService;
    private static UserBackController userBackController;


    @BeforeAll
    public static void setUp() {
        userService = Mockito.mock(UserService.class);
        conversionService = Mockito.mock(ConversionService.class);
        userBackController = new UserBackController(userService,conversionService);
    }

    @Test
    public void addUser() throws Exception {

        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(userService.addUser(aUser())).thenReturn(aUser());
        ResponseEntity<Response> responseEntity = userBackController.addUser(aUser());

        assertEquals(EntityURLBuilder.buildURL2("users", aRate().getId()).toString(),responseEntity.getHeaders().get("Location").get(0));
        assertEquals(HttpStatus.CREATED.value(),responseEntity.getStatusCode().value());
    }

    @Test
    public void getAllUsers() {

        Page<User> userPage = aUserPage();

        when(userService.getAllUsers(any())).thenReturn(userPage);
        ResponseEntity<List<UserDto>> responseEntity = userBackController.getAllUsers(1,1);

        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(userPage.getContent().size(),responseEntity.getBody().size());
        assertEquals(userPage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(userPage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));

    }

    @Test
    public void getAllSorted() throws Exception {

        Page<Rate> ratePage = aRatePage();
        Pageable pageable = PageRequest.of(1,1);

        when(userService.getAllSort(any(),any(),anyList())).thenReturn(aUserPage());
        ResponseEntity<List<UserDto>> responseEntity = userBackController.getAllSorted(pageable.getPageNumber(),pageable.getPageSize(),"firstName","lastName");


        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(ratePage.getContent().size(),responseEntity.getBody().size());
        assertEquals(ratePage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(ratePage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }

    @Test
    public void getAllSpec() {

        Specification<User> userSpecification = Mockito.mock(Specification.class);

        Page<User> userPage = aUserPage();
        Pageable pageable = PageRequest.of(1,1);

        when(userService.getAllSpec(userSpecification,pageable)).thenReturn(userPage);
        ResponseEntity<List<UserDto>> responseEntity = userBackController.getAllSpec(userSpecification,pageable);

        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(userPage.getContent().size(),responseEntity.getBody().size());
        assertEquals(userPage.getTotalElements(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Count").get(0)));
        assertEquals(userPage.getTotalPages(),Long.parseLong(responseEntity.getHeaders().get("X-Total-Pages").get(0)));
    }

    @Test
    public void getUserById() throws Exception {

        when(userService.getUserById(anyInt())).thenReturn(aUser());
        when(conversionService.convert(aUser(),UserDto.class)).thenReturn(aUserDto());
        ResponseEntity<UserDto> responseEntity = userBackController.getUserById(1);

        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(aUser().getId(),responseEntity.getBody().getId());
        assertEquals(aUser().getFirstName(),responseEntity.getBody().getFirstName());
        assertEquals(aUser().getLastName(),responseEntity.getBody().getLastName());
        assertEquals(aUser().getTypeUser(),responseEntity.getBody().getTypeUser());
        assertEquals(aUser().getUsername(),responseEntity.getBody().getUsername());
    }

   /* @Test
    public void login() {

        try {
            Mockito.when(userService.login(any(),any())).thenReturn(aUser());
            Mockito.when(conversionService.convert(aUser(),UserDto.class)).thenReturn(aUserDto());
            ResponseEntity<UserDto> responseEntity = userController.login(aUser().getUsername(),aUser().getPassword());

            assertEquals(aUser().getId(),responseEntity.getBody().getId());
            assertEquals(aUser().getFirstName(),responseEntity.getBody().getFirstName());
            assertEquals(aUser().getLastName(),responseEntity.getBody().getLastName());
            assertEquals(aUser().getTypeUser(),responseEntity.getBody().getTypeUser());
            assertEquals(aUser().getUsername(),responseEntity.getBody().getUsername());
            assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
            assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        }
        catch (ErrorLoginException ex) {
            ex.printStackTrace();
            fail("Fail the tests");
        }

    }*/

    @Test
    public void addAddressToClientUser() throws Exception{
        when(userService.addAddressToClientUser(1,1)).thenReturn(aUser());
        ResponseEntity<Response> responseEntity = userBackController.addAddressToClientUser(1,1);
        assertEquals(HttpStatus.ACCEPTED.value(),responseEntity.getStatusCode().value());
    }

    @Test
    public void deleteUserById() throws Exception {

        Mockito.doNothing().when(userService).deleteById(1);
        ResponseEntity<Object> responseEntity = userBackController.deleteUser(1);
        assertEquals(HttpStatus.ACCEPTED.value(),responseEntity.getStatusCode().value());
    }

}