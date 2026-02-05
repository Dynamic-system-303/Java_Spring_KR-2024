package ru.urfu.spring_urfu.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import ru.urfu.spring_urfu.entity.Role;
import ru.urfu.spring_urfu.repository.RoleRepository;

import java.util.List;

public class DataInitializer {
    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {
            List<String> roles = List.of("ROLE_ADMIN", "ROLE_USER", "ROLE_READ_ONLY");
            for (String roleName : roles) {
                if (roleRepository.findByName(roleName) == null) {
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                }
            }
        };
    }
}
