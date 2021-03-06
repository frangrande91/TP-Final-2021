package edu.utn.TPFinal.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.utn.TPFinal.model.User;
import edu.utn.TPFinal.model.dto.LoginRequestDto;
import edu.utn.TPFinal.model.dto.UserDto;
import edu.utn.TPFinal.model.response.LoginResponseDto;
import edu.utn.TPFinal.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static edu.utn.TPFinal.utils.Constants.JWT_SECRET;

@RestController
@RequestMapping("/login")
public class LoginController {

    private UserService userService;
    private ConversionService conversionService;

    @Autowired
    public LoginController(UserService userService, ConversionService conversionService) {
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @PostMapping(value = "/")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        User user = userService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());
        if (user!=null){
            UserDto dto = conversionService.convert(user,UserDto.class);
            return ResponseEntity.ok(LoginResponseDto.builder().token(this.generateToken(dto)).build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping(value = "/userDetails")
    public ResponseEntity<UserDto> userDetails(Authentication auth) {
        return ResponseEntity.ok((UserDto) auth.getPrincipal());
    }


    public String generateToken(UserDto userDto) {
        try {
            String role = userDto.getTypeUser().toString();
            ObjectMapper objectMapper = new ObjectMapper();
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(role);
            String token = Jwts
                    .builder()
                    .setId("JWT")
                    .setSubject(userDto.getUsername())
                    .claim("user", objectMapper.writeValueAsString(userDto))
                    .claim("authorities",grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + 10000000))
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET.getBytes()).compact();
            return  token;
        } catch(JsonProcessingException e) {
            return "dummy";
        }
    }

}
