package ru.urfu.spring_urfu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.urfu.spring_urfu.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
