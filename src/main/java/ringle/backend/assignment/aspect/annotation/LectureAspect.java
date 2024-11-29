package ringle.backend.assignment.aspect.annotation;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ringle.backend.assignment.api.dto.RequestDto.LectureRequestDto;
import ringle.backend.assignment.aspect.apiPayload.code.status.ErrorStatus;
import ringle.backend.assignment.aspect.apiPayload.exception.handler.TempHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class LectureAspect {

    @Before("@annotation(validateLecture) && args(tutorId, startDate, endDate, ..)")
    public void validateLectureAvailability(ValidateLecture validateLecture,
                                            Long tutorId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 시작날짜와 종료날짜가 요청 시각보다 과거인지 확인
        if (startDate.isBefore(currentDateTime.toLocalDate()) || endDate.isBefore(currentDateTime.toLocalDate()))
            throw new TempHandler(ErrorStatus.LECTURE_FORBIDDEN);

        log.info("유효한 수업 시간대입니다. [Start Date: {}, End Date: {}, Current Date: {}]", startDate, endDate, currentDateTime);
    }
}
