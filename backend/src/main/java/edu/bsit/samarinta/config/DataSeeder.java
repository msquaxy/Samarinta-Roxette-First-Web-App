package edu.bsit.samarinta.config;

import edu.bsit.samarinta.model.User;
import edu.bsit.samarinta.repository.UserRepository;
import edu.bsit.samarinta.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(UserRepository repo, UserService userService) {
        return args -> {
            // Seed default administrator (10 pts requirement)
            String adminEmail = "admin@userhub.com";
            if (!repo.existsByEmail(adminEmail)) {
                User admin = new User(
                        "System Administrator",
                        adminEmail,
                        userService.hash("Admin@123"),
                        "ADMIN"
                );
                repo.save(admin);
                System.out.println("Default ADMIN account seeded: " + adminEmail + " (password: Admin@123)");
            }

            // Also seed demo user from PowerPoint slide 24
            String demoEmail = "ana@bsit.edu";
            if (!repo.existsByEmail(demoEmail)) {
                User demoUser = new User(
                        "Ana Cruz",
                        demoEmail,
                        userService.hash("password123"),
                        "USER"
                );
                repo.save(demoUser);
                System.out.println("Demo USER seeded: " + demoEmail + " (password: password123)");
            }
        };
    }
}
