package com.mynoteservice.config;



import com.mynoteservice.entity.UserEntity;
import com.mynoteservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDefaultUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            String defaultEmail = "admin@mynote.com";

            if (userRepository.findByEmail(defaultEmail).isEmpty()) {
                UserEntity admin = new UserEntity();
                admin.setName("System Admin");
                admin.setEmail(defaultEmail);
                admin.setPassword(passwordEncoder.encode("Admin@12345"));
                admin.setRole("ADMIN");

                userRepository.save(admin);
                System.out.println("Default admin user created successfully!");
            }
        };
    }
}
