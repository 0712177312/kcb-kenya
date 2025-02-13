package com.compulynx.compas.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenRevocationService {
    private Set<String> revokedTokens = new HashSet<>();
    public void revokeToken(String token) {
        revokedTokens.add(token);
    }
    public boolean isTokenRevoked(String token) {
        return revokedTokens.contains(token);
    }
}