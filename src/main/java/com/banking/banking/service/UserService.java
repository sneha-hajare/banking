package com.banking.banking.service;

import com.banking.banking.model.User;
import com.banking.banking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerCustomer(User user) {

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        user.setRole("CUSTOMER");

        return userRepository.save(user);
    }

    public void resetStaffPassword() {

        User staff = userRepository.findByEmail("staff@bankcore.com")
                .orElseThrow(() -> new RuntimeException("Staff user not found"));

        staff.setPassword(
                passwordEncoder.encode("Staff@123")
        );

        staff.setRole("STAFF");

        userRepository.save(staff);
    }
}