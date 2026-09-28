package com.banking.banking.service;

import com.banking.banking.model.User;
import com.banking.banking.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        System.out.println("LOGIN EMAIL RECEIVED: " + email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + email
                        ));

        System.out.println("USER FOUND: " + user.getEmail());
        System.out.println("USER ROLE: " + user.getRole());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(
                        new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole()
                        )
                )
                .build();
    }
}