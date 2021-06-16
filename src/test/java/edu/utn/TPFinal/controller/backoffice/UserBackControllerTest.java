package edu.utn.TPFinal.controller.backoffice;

import edu.utn.TPFinal.controller.backoffice.UserBackController;
import edu.utn.TPFinal.exception.notFound.AddressNotExistsException;
import edu.utn.TPFinal.exception.notFound.ClientNotFoundException;
import edu.utn.TPFinal.exception.notFound.UserNotExistsException;
import edu.utn.TPFinal.model.Rate;
import edu.utn.TPFinal.model.User;
import edu.utn.TPFinal.model.dto.UserDto;
import edu.utn.TPFinal.model.projection.ConsumerProjection;
import edu.utn.TPFinal.model.response.Response;
import edu.utn.TPFinal.service.UserService;
import edu.utn.TPFinal.utils.EntityURLBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.*;

public class UserBackControllerTest {


    private static UserService userService;
    private static ConversionService conversionService;
    private static UserBackController userBackController;


    @BeforeEach
    public void setUp() {
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
    public void getAllSorted() {

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

    @Test
    public void addAddressToClientUser() {
        try {
            when(userService.addAddressToClientUser(1,1)).thenReturn(aUser());
            ResponseEntity<Response> responseEntity = userBackController.addAddressToClientUser(1,1);
            assertEquals(HttpStatus.ACCEPTED.value(),responseEntity.getStatusCode().value());
        } catch (UserNotExistsException | ClientNotFoundException | AddressNotExistsException e) {
            fail(e);
        }
    }

    @Test
    public void deleteUserById() throws Exception {
        try {
            Mockito.doNothing().when(userService).deleteById(1);
            ResponseEntity<Object> responseEntity = userBackController.deleteUser(1);
            assertEquals(HttpStatus.ACCEPTED.value(),responseEntity.getStatusCode().value());
        } catch (UserNotExistsException e) {
            fail(e);
        }
    }

    @Test
    public void get10TopMoreConsumers() {
        ConsumerProjection consumerProjection = mock(ConsumerProjection.class);
        List<ConsumerProjection> consumerProjections = List.of(consumerProjection);
;
        Mockito.when(userService.getTop10MoreConsumers(any(),any())).thenReturn(List.of(consumerProjection));

        ResponseEntity<List<ConsumerProjection>> responseEntity = userBackController.get10TopMoreConsumers(LocalDate.of(2021,8, 1), LocalDate.of(2021,9,1));
        assertEquals(HttpStatus.OK.value(),responseEntity.getStatusCode().value());
        assertEquals(consumerProjections.size(),responseEntity.getBody().size());
    }



}
