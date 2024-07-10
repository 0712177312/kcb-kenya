package com.compulynx.compas.security.service;

import com.compulynx.compas.repositories.UserRepository;
import com.compulynx.compas.security.jwt.JwtTokenUtil;
import com.compulynx.compas.security.model.CustomUserDetails;
import com.compulynx.compas.security.model.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TokenService {
    @Autowired
    private Environment env;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    private static final long expirationMs = 900000; // 1 hour

   // static final String SECRET = "4h1JfF1rkiwYjOzRzZXsBSKSPMZZn4DF77qqIaM9G0Yit3HT3xOJ5zCc5qRSi1n9yZoEGbBLHHq1v6r6HvWgGpq";
	static final String SECRET = "KADJAHDFKAEYAGHKSDYFAEYGSFDBAHSDHFYEAHFYAEJFSLYEHFAGHSYEFAHSBDFHAKYESFSAHFDGAESKYSGHFGAKFGHAKFAFDHGAKFSGHASGHFGAHhkaghfkafgafkaghfdgkfagfhagfhdsf32323347273727adkafhdfakfgKAGFAKDFGA";

    public Authentication parseToken(HttpServletRequest request,HttpServletResponse response) throws IOException {
        try{
            String tokenHeader = request.getHeader("Authorization");

            String token ="";
            if(tokenHeader == null){
                System.out.println("token header is empty!");
                return null;
            }
            else
                token = tokenHeader.replace("Bearer ", "");

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String username = claims.getSubject();

            CustomUserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            }
        }catch (ExpiredJwtException e) {
            handleExpiredToken(request, response);
        } catch (BadCredentialsException e) {
            handleInvalidCredentials(request, response);
        } catch (AuthenticationException e) {
            handleAuthenticationError(request, response, e);
        }
        return null;
    }
    private void handleExpiredToken(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType("application/json");
        Map<String,Object> map = new HashMap<>();
        map.put("message", "Token is expired");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(httpServletResponse.getWriter(), map);
    }

    private void handleInvalidCredentials(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType("application/json");
        Map<String,Object> map = new HashMap<>();
        map.put("message", "Invalid credentials");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(httpServletResponse.getWriter(), map);
    }

    private void handleAuthenticationError(ServletRequest request, ServletResponse response, AuthenticationException e) throws IOException, IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setContentType("application/json");
        Map<String,Object> map = new HashMap<>();
        map.put("message", "Authentication error: " + e.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(httpServletResponse.getWriter(), map);
    }
}
