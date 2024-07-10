package com.compulynx.compas.security.service;

import com.compulynx.compas.models.User;
import com.compulynx.compas.repositories.UserRepository;
import com.compulynx.compas.security.AESsecure;
import com.compulynx.compas.security.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AESsecure aeSsecure;
    @Autowired
    PasswordEncoder encoder;

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User byUsername = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        CustomUserDetails customUserDetails = new CustomUserDetails();
            customUserDetails.setAuthorities(Collections.emptyList());
            customUserDetails.setUsername(byUsername.getUsername());
            customUserDetails.setPassword(byUsername.getPassword());
            customUserDetails.setAccountNonLocked(!byUsername.isLocked());
            customUserDetails.setAccountNonExpired(!byUsername.isLocked());
            customUserDetails.setCredentialsNonExpired(!byUsername.isLocked());
            customUserDetails.setEnabled(byUsername.isStatus());
        return customUserDetails;
    }
}
