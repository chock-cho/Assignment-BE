package ringle.backend.assignment.domain.mapping;

import jakarta.persistence.*;
import lombok.*;
import ringle.backend.assignment.domain.Lecture;
import ringle.backend.assignment.domain.Student;
import ringle.backend.assignment.domain.Tutor;
import ringle.backend.assignment.domain.common.BaseEntity;
import ringle.backend.assignment.domain.enums.LectureType;

/* Reservation 엔티티: 학생의 예약 정보를 관리하는 것에 초점을 둔 엔티티입니다. */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 30분 수업 시: lectureId1 == lectureId2,
    // 60분 수업 시: lectureId1 != lectureId2
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="lecture_id1", nullable = false)
    private Lecture lectureId1;  // 예약된 수업의 1교시(교시:timeSlot)

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="lecture_id2", nullable = false)
    private Lecture lectureId2;  // 예약된 수업의 2교시(교시:timeSlot) (튜터가 미리 열어둔 수업을 학생이 예약한다)

    @Enumerated(EnumType.STRING)
    @Column
    private LectureType lectureType; // 예약한 수업이 30분인지 60분인지 확인해준다.

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="student_id")
    private Student student;  // 예약된 학생 (1대1 화상 수업 관계)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tutor_id")
    private Tutor tutor; // 예약된 수업의 튜터

}

