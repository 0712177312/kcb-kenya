//package com.compulynx.compas.configs;
//
//import java.io.IOException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class RestConfigs extends OncePerRequestFilter {
//
//    @Override
//    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
//                                    final FilterChain filterChain) throws ServletException, IOException {
//
//        response.addHeader("Access-Control-Allow-Methods", "GET, POST");
//        response.addHeader("Access-Control-Allow-Headers","headers, x-xsrf-token, Origin, Content-Type, Accept, Authorization, X-Requested-With, timeout, Access-Control-Expose-Headers, X-Requested-With,Content-Type, Access-Control-Request-Method,  Access-Control-Request-Headers, key, encKey, Key");
//        response.addHeader("Access-Control-Max-Age", "3600");
//        // Added HTTP headers for security vulnerability
//        response.addHeader("Access-Control-Allow-Origin","http://localhost:4200");
//        response.addHeader("X-Frame-Options","DENY");
//        if (request.isSecure()) {
//            response.addHeader("Strict-Transport-Security","max-age=31536000; includeSubDomains");
//        }
//        response.addHeader("X-Content-Type-Options","nosniff");
//        response.addHeader("X-XSS-Protection","1; mode=block");
//        response.addHeader("Cache-Control","no-store");
//        response.addHeader("Pragma"," no-cache");
//        // For HTTP OPTIONS verb/method reply with ACCEPTED status code -- per CORS
//        // handshake
//        if (request.getMethod().equals("OPTIONS")) {
//            response.setStatus(HttpServletResponse.SC_ACCEPTED);
//            return;
//        }
//        response.addHeader("Access-Control-Expose-Headers", "*");
//        response.addHeader("Access-Control-Allow-Credentials", "true");
//        filterChain.doFilter(request, response);
//    }
//}
