package ringle.backend.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ringle.backend.assignment.domain.mapping.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
