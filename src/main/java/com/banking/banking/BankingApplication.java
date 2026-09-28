package com.banking.banking;

import com.banking.banking.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
    }

    @Bean
    CommandLineRunner resetStaffPassword(UserService userService) {
        return args -> {
            userService.resetStaffPassword();
            System.out.println("========== STAFF PASSWORD RESET DONE ==========");
        };
    }
}