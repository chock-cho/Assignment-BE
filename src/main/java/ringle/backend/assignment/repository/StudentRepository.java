package ringle.backend.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ringle.backend.assignment.domain.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
