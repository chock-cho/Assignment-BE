package ringle.backend.assignment.service.ReservationService;

import ringle.backend.assignment.api.dto.ResponseDto.ReservationResponseDto;

import java.util.List;

public interface ReservationQueryService {
    List<ReservationResponseDto.ReservationInfoResponse> getMyReservations(Long studentId);
}
