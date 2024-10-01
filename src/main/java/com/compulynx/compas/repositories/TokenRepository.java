package com.compulynx.compas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.compulynx.compas.models.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
   

//   @Query(nativeQuery = true, value="SELECT * FROM Token  ORDER BY created_at DESC")
//   Token findLatestToken();
}
