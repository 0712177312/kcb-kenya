package com.compulynx.compas.security.filters;

import com.compulynx.compas.security.service.TokenRevocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    public CustomLogoutSuccessHandler(){
    }
    @Autowired
    private TokenRevocationService tokenRevocationService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String tokenHeader = request.getHeader("Authorization");
        String token = "";
        if(tokenHeader != null){
            token = tokenHeader.replace("Bearer ", "");
            tokenRevocationService.revokeToken(token);
            handleLogoutSuccess(request,response);
        }
    }
    private void handleLogoutSuccess(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType("application/json");
        Map<String, Object> map = new HashMap<>();
        map.put("message", "Logout Sucess!");
        map.put("statusCode", "200");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(httpServletResponse.getWriter(), map);
    }
}
