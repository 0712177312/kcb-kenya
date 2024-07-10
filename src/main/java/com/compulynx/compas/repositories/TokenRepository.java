package com.compulynx.compas.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.compulynx.compas.models.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
//	@Query("SELECT  FROM Token  ORDER BY createdAt DESC")
//    Optional<Token> findLatestToken();
}
