package ringle.backend.assignment.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ringle.backend.assignment.domain.common.BaseEntity;
import ringle.backend.assignment.domain.enums.LectureType;
import ringle.backend.assignment.domain.enums.TimeSlot;
import ringle.backend.assignment.domain.mapping.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

/*  Lecture 엔티티: 튜터의 수업 가능 시간을 관리하는 역할에 집중합니다. */
@Entity
@Getter
@Setter
@Table(
        uniqueConstraints = {@UniqueConstraint(columnNames = {"tutor_id", "date", "startTimeSlot"})},
        // 제약 조건: 각각의 수업은 한 명의 튜터에 대해, {수업 날짜 & 수업 시작 시각}이 유일하여야 한다.
        indexes = {@Index(name = "idx_tutor_date_timeslot", columnList = "tutor_id, date, startTimeSlot, isAvailable"),
                @Index(name = "idx_date_lectureType", columnList = "date, lectureType, isAvailable"),
                @Index(name = "idx_timeslot_lectureType", columnList = "startTimeSlot, lectureType, isAvailable")
                // 가능한 수업 조회 성능 개선을 위해,
                // 1. {튜터 id & 시작 시각 & 시작 time slot & 예약 가능 여부} 기준으로 복합 인덱스 설정
                // 2. {기간 & 수업 길이 & 예약 가능 여부} 기준으로 복합 인덱스 설정
                // 2. {시간대 & 수업 길이 & 예약 가능 여부} 기준으로 복합 인덱스 설정
        }
)
public class Lecture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();  // 수업 날짜 지정(2024-11-28), 기본값: 서버 타임 상 오늘

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeSlot startTimeSlot;  // 수업 시작

    @Enumerated(EnumType.STRING)
    @Column
    private LectureType lectureType;  // 수업 길이 (30분 또는 60분)

    @Column(nullable = false)
    private boolean isAvailable;  // 예약 가능 여부

    // 튜터가 수업을 열고, 학생이 신청하는 구조
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name="tutor_id")
    private Tutor tutor;  // 수업을 가르치는 튜터(필수)

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="reservation_id")
    private Reservation reservation;  // 수업에 대한 예약 정보 (예약된 경우에만 존재)

    // 낙관적 락-- 버전 필드
    @Version
    @Column
    private Integer version;

    // TimeSlot을 LocalTime으로 변환하는 메서드
    public LocalTime toLocalTime() {
        int hour = (this.startTimeSlot.getOrder() - 1) / 2; // order 값을 기준으로 시간 계산
        int minute = (this.startTimeSlot.getOrder() - 1) % 2 * 30; // 분 계산
        return LocalTime.of(hour, minute);
    }
}