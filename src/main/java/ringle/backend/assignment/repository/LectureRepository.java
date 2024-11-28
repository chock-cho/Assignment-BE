package ringle.backend.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ringle.backend.assignment.domain.Lecture;
import ringle.backend.assignment.domain.Tutor;
import ringle.backend.assignment.domain.enums.LectureType;
import ringle.backend.assignment.domain.enums.TimeSlot;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {

    boolean existsByTutorAndDateAndStartTimeSlot(Tutor tutor, LocalDate date, TimeSlot startTimeSlot);

    // {기간 & 수업 길이} 로 예약 가능한 수업(타임슬롯)을 조회하는 메서드
    //List<Lecture> findByDateBetweenAndLectureTypeAndIsAvailableTrue(LocalDate startDate, LocalDate endDate, LectureType lectureType);

    // {시간대 & 수업 길이} 로 수업 가능한 튜터를 조회하는 메서드
    List<Lecture> findByStartTimeSlotAndLectureTypeAndIsAvailableTrue(TimeSlot timeSlot, LectureType lectureType);

    // 튜터 정보 반환 시, 수업이 시작되는 시간대를 기반으로 앞뒤 타임 슬롯의 수업 가능 상태 조회
    Optional<Lecture> findByTutorAndDateAndStartTimeSlot(Tutor tutor, LocalDate date, TimeSlot previousSlot);
}
