package ringle.backend.assignment.service.ReservationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ringle.backend.assignment.api.dto.ResponseDto.ReservationResponseDto;
import ringle.backend.assignment.domain.mapping.Reservation;
import ringle.backend.assignment.repository.ReservationRepository;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ReservationQueryServiceImpl implements ReservationQueryService {
    private final ReservationRepository reservationRepository;
    @Override
    @Transactional(readOnly=true) // readOnly = true : 읽기 전용 트랜잭션 추가
    public List<ReservationResponseDto.ReservationInfoResponse> getMyReservations(Long studentId) {
        // 학생 ID로 예약 내역 조회
        List<Reservation> myReservations = reservationRepository.findByStudentId(studentId);

        // 조회된 데이터를 ReservationResponseDto로 변환
        return myReservations.stream()
                .map(reservation -> {
                    // 예약된 강의 타입 확인
                    String reservedLectureType = reservation.getLectureType().name();

                    // LectureInfo 빌드
                    ReservationResponseDto.ReservationInfoResponse.LectureInfo lectureInfo = ReservationResponseDto.ReservationInfoResponse.LectureInfo.builder()
                            .startLecture(
                                    ReservationResponseDto.ReservationInfoResponse.LectureDetail.builder()
                                            .lectureId(reservation.getLectureId1().getId())
                                            .lectureTimeSlot(reservation.getLectureId1().getStartTimeSlot().name())
                                            .build()
                            )
                            .endLecture(
                                    // lectureType이 _30_MIN이면 endLecture는 포함하지 않음
                                    "_30_MIN".equals(reservedLectureType) ? null :
                                            ReservationResponseDto.ReservationInfoResponse.LectureDetail.builder()
                                                    .lectureId(reservation.getLectureId2().getId())
                                                    .lectureTimeSlot(reservation.getLectureId2().getStartTimeSlot().name())
                                                    .build()
                            )
                            .build();

                    // ReservationResponseDto 빌드
                    return ReservationResponseDto.ReservationInfoResponse.builder()
                            .reservationId(reservation.getId())
                            .reservedLectureType(reservedLectureType)
                            .tutorInfo(
                                    ReservationResponseDto.ReservationInfoResponse.TutorInfo.builder()
                                            .tutorId(reservation.getTutor().getId())
                                            .tutorName(reservation.getTutor().getName())
                                            .build()
                            )
                            .lectureInfo(lectureInfo)
                            .build();
                })
                .toList();
    }
}
