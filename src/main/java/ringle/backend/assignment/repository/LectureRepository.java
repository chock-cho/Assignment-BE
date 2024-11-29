package ringle.backend.assignment.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ringle.backend.assignment.domain.Lecture;
import ringle.backend.assignment.domain.Tutor;
import ringle.backend.assignment.domain.enums.LectureType;
import ringle.backend.assignment.domain.enums.TimeSlot;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    boolean existsByTutorAndDateAndStartTimeSlot(Tutor tutor, LocalDate date, TimeSlot startTimeSlot);

    // 튜터 정보 반환 시, 수업이 시작되는 시간대를 기반으로 앞뒤 타임 슬롯의 수업 가능 상태 조회
    Optional<Lecture> findByTutorAndDateAndStartTimeSlot(Tutor tutor, LocalDate date, TimeSlot previousSlot);

    // 60 MIN 수업 선택 시, 타임 슬롯 2개가 연속적으로 가능한 상태여야 함
    boolean existsByTutorAndDateAndStartTimeSlotAndIsAvailableTrue(Tutor tutor, LocalDate date, TimeSlot nextSlot);

    // { 기간(시작 날짜, 종료 날짜) & 수업 길이} 로 '가능한 수업 & 튜터(Tutor)'을 조회하는 메서드
    List<Lecture> findByTutorAndDateAndStartTimeSlotAndIsAvailableTrue(Tutor tutor, LocalDate date, TimeSlot timeSlot);

    @Transactional
    @Modifying
    @Query("DELETE FROM Lecture l WHERE l.reservation IS NULL AND l.date < CURRENT_DATE")
    int deleteUnreservedLectures();
}
