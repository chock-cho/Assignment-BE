package ringle.backend.assignment.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ringle.backend.assignment.domain.common.BaseEntity;
import ringle.backend.assignment.domain.mapping.Reservation;

import java.util.List;

@Entity
@Getter
@Setter
public class Student extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, length = 20)
    private String password;

    @Column
    private String phoneNum;

    @Column(nullable = false, length = 50)
    private String name;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reservation> reservedLectures;
}
