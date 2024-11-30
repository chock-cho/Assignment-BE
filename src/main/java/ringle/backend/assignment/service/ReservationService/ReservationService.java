package ringle.backend.assignment.service.ReservationService;

import ringle.backend.assignment.api.dto.RequestDto.ReservationRequestDto;
import ringle.backend.assignment.api.dto.ResponseDto.ReservationResponseDto;

import java.util.List;

public interface ReservationService {
    ReservationResponseDto.ReservationCreateResponse makeReservation(Long studentId, ReservationRequestDto.ReservationCreateRequest req);
}
