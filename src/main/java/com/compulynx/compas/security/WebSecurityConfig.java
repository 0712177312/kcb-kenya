package com.compulynx.compas.security;
//https://panlw.github.io/15359384213292.html

import com.compulynx.compas.security.exceptions.CustomAccessDeniedHandler;
import com.compulynx.compas.security.filters.CustomLogoutSuccessHandler;
import com.compulynx.compas.security.filters.JwtAthenticationFiler;
import com.compulynx.compas.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //@Autowired
    //CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    private JwtAthenticationFiler jwtAthenticationFiler;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/", "/ping", "/rest/v1/dashboard/configs", "/rest/v1/auth/manual_login","/rest/v1/auth/bio_login","/rest/v1/auth/verify_abisclient_token","/rest/v1/abisClient_encrypt_decrypt/decrypt","/rest/v1/abisClient_encrypt_decrypt/encrypt").permitAll()
                .antMatchers(HttpMethod.GET, "/*.js", "/*.woff", "/*.ttf", "/*.woff2", "/*.png", "/*.css", "/*.map").permitAll()
                .antMatchers(HttpMethod.GET, "/assets/**").permitAll()
               // .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl("/logout") // URL to trigger logout
                .logoutSuccessHandler(logoutSuccessHandler()) // Logout success handler
                .invalidateHttpSession(true) // invalidate session
                .deleteCookies("JSESSIONID")
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtAthenticationFiler, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);

        http.headers()
                .httpStrictTransportSecurity()
                .includeSubDomains(true)
                .maxAgeInSeconds(31536000);

        // Additional header for xss
        http.headers().xssProtection();

        // Additional header for x-frame-options
        http
                .headers()
                .frameOptions()
                .sameOrigin();
    }

    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //auth.authenticationProvider(daoAuthenticationProvider());
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler(); // Implement your own LogoutSuccessHandler if needed
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new
                UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://10.216.1.74:8440","http://localhost:4200"));
        config.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setExposedHeaders(Arrays.asList("encKey"));
        config.setMaxAge(3600L);
        config.setAllowedHeaders(Arrays.asList("headers", "x-xsrf-token", "Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With", "timeout", "Access-Control-Expose-Headers", "X-Requested-With,Content-Type", "Access-Control-Request-Method",  "Access-Control-Request-Headers", "key", "encKey", "Key"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
