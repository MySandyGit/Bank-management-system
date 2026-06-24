package com.bank;

import com.bank.entity.Role;
import com.bank.entity.User;
import com.bank.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BankManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankManagementSystemApplication.class, args);
    }

    /**
     * Runs once when the app starts.
     * Creates a default ADMIN account (if it doesn't already exist) so you
     * have a way to log in as admin right away.
     */
    @Bean
    public CommandLineRunner seedAdmin(UserRepository userRepository,
                                        PasswordEncoder passwordEncoder,
                                        @Value("${app.admin.username}") String adminUsername,
                                        @Value("${app.admin.password}") String adminPassword) {
        return args -> {
            if (!userRepository.existsByUsername(adminUsername)) {
                User admin = new User(adminUsername, passwordEncoder.encode(adminPassword), Role.ADMIN);
                userRepository.save(admin);
                System.out.println("==============================================");
                System.out.println(" Default admin created -> username: " + adminUsername + " | password: " + adminPassword);
                System.out.println("==============================================");
            }
        };
    }
}
