package ru.urfu.spring_urfu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.spring_urfu.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
