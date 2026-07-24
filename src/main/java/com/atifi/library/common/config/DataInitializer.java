package com.atifi.library.common.config;

import com.atifi.library.auth.model.Role;
import com.atifi.library.auth.model.User;
import com.atifi.library.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository repository,
                           PasswordEncoder encoder) {

        return args -> {

            if (repository.count() == 0) {

                User admin = new User();

                admin.setUsername("admin");
                admin.setPassword(encoder.encode("1234"));
                admin.setRole(Role.ADMIN);

                repository.save(admin);

            }

        };

    }
}
