package ru.urfu.spring_urfu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.spring_urfu.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
