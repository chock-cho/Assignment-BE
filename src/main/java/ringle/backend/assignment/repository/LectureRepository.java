package ringle.backend.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ringle.backend.assignment.domain.Lecture;
import ringle.backend.assignment.domain.Tutor;
import ringle.backend.assignment.domain.enums.TimeSlot;

import java.time.LocalDate;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    boolean existsByTutorAndDateAndTimeSlot(Tutor tutor, LocalDate date, TimeSlot timeSlot);
}
