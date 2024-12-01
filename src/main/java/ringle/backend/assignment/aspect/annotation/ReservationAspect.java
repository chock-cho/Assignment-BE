package ringle.backend.assignment.aspect.annotation;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ringle.backend.assignment.api.dto.RequestDto.ReservationRequestDto;
import ringle.backend.assignment.aspect.apiPayload.code.status.ErrorStatus;
import ringle.backend.assignment.aspect.apiPayload.exception.handler.TempHandler;
import ringle.backend.assignment.domain.Lecture;
import ringle.backend.assignment.domain.mapping.Reservation;
import ringle.backend.assignment.repository.LectureRepository;

import java.time.LocalTime;


@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class ReservationAspect {
    private final LectureRepository lectureRepository;
    @Around("@annotation(validateReservation) && args(req, ..)")
    public Object validateReservationAvailability(
            ProceedingJoinPoint joinPoint,
            ValidateReservation validateReservation,
            ReservationRequestDto.ReservationCreateRequest req) throws Throwable {
        Lecture startLecture = lectureRepository.findById(req.getStartLectureId())
                .orElseThrow(()-> new TempHandler(ErrorStatus.RESERVATION_NOT_FOUND));

        LocalTime startTime = startLecture.getStartTimeSlot().toLocalTime();
        LocalTime cutoffTime = startTime.minusMinutes(30);
        LocalTime requestTime = LocalTime.now();


        if (requestTime.isAfter(cutoffTime))
            throw new TempHandler(ErrorStatus.RESERVATION_FORBIDDEN);
        // 현재 시각을 기준으로 30분 미만 남아있는 시점에서 수업 예약 요청이라면, 실패

        return joinPoint.proceed();
    }
}
