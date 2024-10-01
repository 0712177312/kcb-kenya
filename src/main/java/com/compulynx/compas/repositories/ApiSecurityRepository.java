package com.compulynx.compas.repositories;

import com.compulynx.compas.models.ApiSecurity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.lang.String;
import java.util.List;

public interface ApiSecurityRepository extends JpaRepository<ApiSecurity, Long> {

    @Query("select apisecurity from ApiSecurity apisecurity where apisecurity.username =:username and apisecurity.password =:password")
    ApiSecurity Authenticate(@Param("username") String username,@Param("password") String password);
    List<ApiSecurity> findByUsername(String username);
}