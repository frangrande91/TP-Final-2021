package edu.utn.TPFinal.filter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.utn.TPFinal.model.dto.UserDto;
import io.jsonwebtoken.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static edu.utn.TPFinal.utils.Constants.*;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    ObjectMapper objectMapper;

    public JWTAuthorizationFilter() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Internal JWT Filter to check if the request is valid
     *
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            if (containsJWT(request, response)) {
                Claims claims = validateToken(request);
                if (claims.get("user") != null) {
                    setUpSpringAuthentication(claims);
                } else {
                    SecurityContextHolder.clearContext();
                }
            } else {
                SecurityContextHolder.clearContext();
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
            return;
        }
    }


    /**
     * Method to validate if the token is valid
     *
     * @param request
     * @return
     */

    private Claims validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(JWT_HEADER).replace(JWT_PREFIX, "");
        return Jwts.parser().setSigningKey(JWT_SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
    }

    /**
     * Authentication Method to authorize through Spring
     *
     * @param claims
     */
    private void setUpSpringAuthentication(Claims claims) {
        try {
            List<String> authorities = (List) claims.get("authorities");
            String userClaim = (String) claims.get("user");
            UserDto user = objectMapper.readValue(userClaim, UserDto.class);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
                    authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (JsonProcessingException e) {
            SecurityContextHolder.clearContext();
        }


    }

    private boolean containsJWT(HttpServletRequest request, HttpServletResponse res) {
        String authenticationHeader = request.getHeader(JWT_HEADER);
        if (authenticationHeader == null || !authenticationHeader.startsWith(JWT_PREFIX))
            return false;
        return true;
    }
}
