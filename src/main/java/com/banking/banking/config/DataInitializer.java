package com.banking.banking.config;

import com.banking.banking.model.User;
import com.banking.banking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createDefaultUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (!userRepository.existsByEmail("admin@bankcore.com")) {

                User admin = new User();

                admin.setName("Bank Administrator");
                admin.setEmail("admin@bankcore.com");
                admin.setPhone("9999999999");
                admin.setPassword(
                        passwordEncoder.encode("admin123")
                );
                admin.setRole("ADMIN");

                userRepository.save(admin);
            }

            if (!userRepository.existsByEmail("staff@bankcore.com")) {

                User staff = new User();

                staff.setName("Bank Staff");
                staff.setEmail("staff@bankcore.com");
                staff.setPhone("8888888888");
                staff.setPassword(
                        passwordEncoder.encode("staff123")
                );
                staff.setRole("STAFF");

                userRepository.save(staff);
            }
        };
    }
}