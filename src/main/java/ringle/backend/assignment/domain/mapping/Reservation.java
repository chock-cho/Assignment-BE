package ringle.backend.assignment.domain.mapping;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ringle.backend.assignment.domain.Lecture;
import ringle.backend.assignment.domain.Student;
import ringle.backend.assignment.domain.Tutor;
import ringle.backend.assignment.domain.common.BaseEntity;

/* Reservation 엔티티: 학생의 예약 정보를 관리하는 것에 초점을 둔 엔티티입니다. */
@Entity
@Getter
@Setter
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="lecture_id")
    private Lecture lecture;  // 예약된 수업 (튜터가 미리 열어둔 수업을 학생이 예약한다)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="student_id")
    private Student student;  // 예약된 학생 (1대1 화상 수업 관계)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;
}

