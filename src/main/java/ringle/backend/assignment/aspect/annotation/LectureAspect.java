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

    @Before("@annotation(validateLecture) && args(req, ..)")
    public void validateLectureAvailability(JoinPoint joinPoint, ValidateLecture validateLecture, LectureRequestDto.LectureCreateRequest req) {
        LocalDate startDate = req.getStartDate();
        LocalDate endDate = req.getEndDate();
        LocalDateTime currentDateTime = LocalDateTime.now();

        if (startDate.isBefore(currentDateTime.toLocalDate()) || endDate.isBefore(currentDateTime.toLocalDate()))
            throw new TempHandler(ErrorStatus.LECTURE_FORBIDDEN);

        log.info("유효한 수업 시간대입니다. [Start Date: {}, End Date: {}, Current Date: {}]", startDate, endDate, currentDateTime);
    }
}
