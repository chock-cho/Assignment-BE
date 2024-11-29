package ringle.backend.assignment.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ringle.backend.assignment.domain.Tutor;

import java.util.List;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    @Query("SELECT t FROM Tutor t WHERE t.id IN ( SELECT l.tutor.id FROM Lecture l  WHERE l.isAvailable = true)")
    List<Tutor> findAllWithAvailableLectures();
}
