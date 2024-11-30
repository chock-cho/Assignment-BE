package ringle.backend.assignment.service.ReservationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ringle.backend.assignment.api.dto.ResponseDto.ReservationResponseDto;
import ringle.backend.assignment.converter.ReservationConverter;
import ringle.backend.assignment.domain.mapping.Reservation;
import ringle.backend.assignment.repository.ReservationRepository;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ReservationQueryServiceImpl implements ReservationQueryService {
    private final ReservationRepository reservationRepository;
    private final ReservationConverter reservationConverter;

    @Override
    @Transactional(readOnly=true) // readOnly = true : 읽기 전용 트랜잭션 추가
    public List<ReservationResponseDto.ReservationInfoResponse> getMyReservations(Long studentId) {
        // 학생 ID로 예약 내역 조회
        List<Reservation> myReservations = reservationRepository.findByStudentId(studentId);
        return reservationConverter.toReservationInfoResponses(myReservations);
    }
}
