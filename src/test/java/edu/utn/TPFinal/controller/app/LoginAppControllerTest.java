package edu.utn.TPFinal.controller.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.utn.TPFinal.model.User;
import edu.utn.TPFinal.model.dto.UserDto;
import edu.utn.TPFinal.model.response.LoginResponseDto;
import edu.utn.TPFinal.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import static edu.utn.TPFinal.utils.Constants.JWT_SECRET;
import static edu.utn.TPFinal.utils.UserTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginAppControllerTest {

    private static UserService userService;
    private static ConversionService conversionService;
    private static LoginAppController loginAppController;


    @BeforeAll
    public static void setUp(){
        userService = mock(UserService.class);
        conversionService = mock(ConversionService.class);
        loginAppController = new LoginAppController(userService, conversionService);
    }


    @Test
    public void loginOk(){
        when(userService.login("nahuelmdp", "1234")).thenReturn(aUser());
        when(conversionService.convert(aUser(), UserDto.class)).thenReturn(aUserDto());
        String token = loginAppController.generateToken(aUserDto());
        LoginResponseDto loginResponseDto = LoginResponseDto.builder().token(token).build();

        ResponseEntity<LoginResponseDto> response = loginAppController.login(aLoginRequestDto());

        assertEquals(HttpStatus.OK, response.getStatusCode());
       //assertEquals(loginResponseDto, response.getBody());
    }


    @Test
    public void loginUnauthorized(){
        when(userService.login("nahuelmdp", "1234")).thenReturn(null);

        ResponseEntity<LoginResponseDto> response = loginAppController.login(aLoginRequestDto());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    public void userDetails(){
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(aUser());

        ResponseEntity<User> response = loginAppController.userDetails(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(aUser(), response.getBody());
    }

    @Test
    public void generateToken() {
        String role = aUserDto().getTypeUser().toString();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //AuthorityUtils authorityUtils = mock(AuthorityUtils.class);
            //GrantedAuthority grantedAuthority = mock(GrantedAuthority.class);
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(role);
            //Jwts jwts = mock(Jwts.class);
            String token = Jwts
                    .builder()
                    .setId("JWT")
                    .setSubject(aUserDto().getUsername())
                    .claim("user", objectMapper.writeValueAsString(aUserDto()))
                    .claim("authorities", grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000000))
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET.getBytes()).compact();

            String tokenReturn = loginAppController.generateToken(aUserDto());

            assertEquals(token.length(), tokenReturn.length());
        } catch (Exception e) {
            fail(e);
        }
    }

    /*@Test
    public void generateTokenFail() throws JsonProcessingException {
        String role = aUserDto().getTypeUser().toString();

    //    ObjectMapper objectMapper = new ObjectMapper();
        ObjectMapper objectMapper = mock(ObjectMapper.class);

        //AuthorityUtils authorityUtils = mock(AuthorityUtils.class);
        //GrantedAuthority grantedAuthority = mock(GrantedAuthority.class);
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(role);
        //Jwts jwts = mock(Jwts.class);
        doThrow(new JsonProcessingException(""){}).when(objectMapper).writeValueAsString(aUserDto());

        try {
            String token = Jwts
                    .builder()
                    .setId("JWT")
                    .setSubject(aUserDto().getUsername())
                    .claim("user", objectMapper.writeValueAsString(aUserDto()))
                    .claim("authorities", grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 1000000))
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET.getBytes()).compact();

            String tokenReturn = loginAppController.generateToken(aUserDto());
        } catch (JsonProcessingException e) {

        }

    }*/





}
