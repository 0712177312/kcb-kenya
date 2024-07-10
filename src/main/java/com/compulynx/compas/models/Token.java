package com.compulynx.compas.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Token  {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
	    @SequenceGenerator(name = "token_seq", sequenceName = "token_id_seq", allocationSize = 1)
	    private Long id;

	    @Column(name = "access_token")
	    private String accessToken;
	    
	    @Column(name = "created_at")
	    private LocalDateTime createdAt;
	    
	    @Column(name = "expires_in")
	    private long expiresIn;
	    // Constructors
	    public Token() {}

	
		public String getAccessToken() {
			return accessToken;
		}
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
	
	    public LocalDateTime getCreatedAt() {
	        return createdAt;
	    }

	    public void setCreatedAt(LocalDateTime createdAt) {
	        this.createdAt = createdAt;
	    }
	    
	

		public Long getId() {
			return id;
		}


		public void setId(Long id) {
			this.id = id;
		}


		public long getExpiresIn() {
			return expiresIn;
		}


		public void setExpiresIn(long expiresIn) {
			this.expiresIn = expiresIn;
		}


		// Method to check if the token has expired
	    public boolean isExpired() {
	        return LocalDateTime.now().isAfter(createdAt.plusSeconds(expiresIn));
	    }   
	    
}
