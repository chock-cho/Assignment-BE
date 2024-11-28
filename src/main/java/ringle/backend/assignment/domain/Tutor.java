package ringle.backend.assignment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ringle.backend.assignment.domain.common.BaseEntity;

import java.util.List;

@Entity
@Getter
@Setter
public class Tutor extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username; // 이메일

    @Column(nullable = false, length = 20)
    private String password; // 패스워드

    @Column(nullable = false, length = 50)
    private String name;

    @Column
    private String university; // 소속 대학

    @Column
    private String major; // 전공

    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Lecture> lectures;  // 튜터가 여는 수업 목록 (학생 여부와 상관없이, 자신이 연 모든 수업 목록 조회 가능)

    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
    private List<Reservation> confirmedLectures;  // 튜터의 예약된 수업 목록 (학생이 할당되어 activate해야 하는 수업 목록)
}
