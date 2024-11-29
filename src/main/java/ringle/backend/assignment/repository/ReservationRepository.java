package ringle.backend.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ringle.backend.assignment.domain.mapping.Reservation;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByStudentId(Long studentId);
}