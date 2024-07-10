package com.compulynx.compas.security.filters;

import com.compulynx.compas.security.jwt.JwtTokenUtil;
import com.compulynx.compas.security.model.CustomUserDetails;
import com.compulynx.compas.security.service.CustomUserDetailsService;
import com.compulynx.compas.security.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAthenticationFiler extends OncePerRequestFilter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;


    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private Environment env;

   // static final String SECRET = "4h1JfF1rkiwYjOzRzZXsBSKSPMZZn4DF77qqIaM9G0Yit3HT3xOJ5zCc5qRSi1n9yZoEGbBLHHq1v6r6HvWgGpq";
	static final String SECRET = "KADJAHDFKAEYAGHKSDYFAEYGSFDBAHSDHFYEAHFYAEJFSLYEHFAGHSYEFAHSBDFHAKYESFSAHFDGAESKYSGHFGAKFGHAKFAFDHGAKFSGHASGHFGAHhkaghfkafgafkaghfdgkfagfhagfhdsf32323347273727adkafhdfakfgKAGFAKDFGA";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setContentType("UTF-8");
        try {
            String tokenHeader = request.getHeader("Authorization");
            String token = "";
            if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
                token = tokenHeader.replace("Bearer ", "");
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(SECRET)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                    if (jwtTokenUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        usernamePasswordAuthenticationToken
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                    }else{
                        SecurityContextHolder.getContext().setAuthentication(null);
                    }
                }

            }
            filterChain.doFilter(request, response);
        } catch (SignatureException signatureException) {
            signatureException.getMessage();
            handleSignatureException(request, response);
        } catch (ExpiredJwtException e) {
            handleExpiredToken(request, response);
        } catch (BadCredentialsException e) {
            handleInvalidCredentials(request, response);
        } catch (AuthenticationException e) {

            handleAuthenticationError(request, response, e);
        } catch (AccessDeniedException s) {
        }
    }
    private void handleExpiredToken(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType("application/json");
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Token is expired");
        map.put("statusCode", "401");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(httpServletResponse.getWriter(), map);
    }

    private void handleInvalidCredentials(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType("application/json");
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Invalid credentials");
        map.put("statusCode", "401");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(httpServletResponse.getWriter(), map);
    }

    private void handleAuthenticationError(ServletRequest request, ServletResponse response, AuthenticationException e) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType("application/json");
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Authentication error: " + e.getMessage());
        map.put("statusCode", "401");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(httpServletResponse.getWriter(), map);
    }

    private void handleSignatureException(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType("application/json");
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Invalid Token Signature ");
        map.put("statusCode", "401");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(httpServletResponse.getWriter(), map);
    }
    private void handleUnauthenticated(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType("application/json");
        Map<String, Object> map = new HashMap<>();
        map.put("message", "You're not authenticated! ");
        map.put("statusCode", "401");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(httpServletResponse.getWriter(), map);
    }
}
