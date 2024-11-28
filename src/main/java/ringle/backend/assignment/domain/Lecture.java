package ringle.backend.assignment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ringle.backend.assignment.domain.common.BaseEntity;
import ringle.backend.assignment.domain.enums.Duration;
import ringle.backend.assignment.domain.enums.TimeSlot;
import ringle.backend.assignment.domain.mapping.Reservation;

import java.time.LocalDate;
/*  Lecture 엔티티: 튜터의 수업 가능 시간을 관리하는 역할에 집중합니다. */
@Entity
@Getter
@Setter
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"date", "timeSlot"})})
public class Lecture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date = LocalDate.now();  // 수업 날짜 지정(2024-11-28), 기본값: 서버 타임 상 오늘

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeSlot timeSlot;  // 수업 시작

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Duration duration;  // 수업 길이 (30분 또는 60분)

    @Column(nullable = false)
    private boolean isAvailable;  // 예약 가능 여부

    // 튜터가 수업을 열고, 학생이 신청하는 구조
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name="tutor_id")
    private Tutor tutor;  // 수업을 가르치는 튜터(필수)

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="reservation_id")
    private Reservation reservation;  // 수업에 대한 예약 정보 (예약된 경우에만 존재)
}