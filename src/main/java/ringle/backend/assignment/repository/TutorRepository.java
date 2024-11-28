package ringle.backend.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ringle.backend.assignment.domain.Tutor;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
}
