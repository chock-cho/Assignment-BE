package ringle.backend.assignment.aspect.annotation;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ringle.backend.assignment.aspect.apiPayload.code.status.ErrorStatus;
import ringle.backend.assignment.aspect.apiPayload.exception.handler.TempHandler;
import ringle.backend.assignment.domain.Lecture;
import ringle.backend.assignment.domain.mapping.Reservation;

import java.time.LocalTime;


@Aspect
@Component
@Slf4j
public class ReservationAspect {
    @Before("@annotation(validateReservation) && args(reservation, ..)")
    public void validateReservationAvailability(ValidateReservation validateReservation, Reservation reservation) {
        Lecture lecture1 = reservation.getLectureId1();
        Lecture lecture2 = reservation.getLectureId2();


        if(lecture1 == null || lecture2 == null)
            throw new TempHandler(ErrorStatus.RESERVATION_NOT_FOUND); // 수업 예약 요청에 해당하는 가능한 타임 슬롯이 없음

        LocalTime classTime = lecture1.toLocalTime(); // 열려 있는 타임 슬롯
        LocalTime cutoffTime = classTime.minusMinutes(30); // cutOffTime: 현재 요청 시간 수업 시작 30분 전

        LocalTime requestTime = reservation.getCreatedAt().toLocalTime();

        if (requestTime.isAfter(cutoffTime))
            throw new TempHandler(ErrorStatus.RESERVATION_FORBIDDEN);
        // 현재 시각을 기준으로 30분 미만 남아있는 시점에서 수업 예약 요청이라면, 실패

        log.info("예약이 가능합니다. [Lecture ID: {}, Request Time: {}]", reservation.getLectureId1().getId(), requestTime);
    }
}
