package ringle.backend.assignment.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateReservation {
    // 예약 요청 시각을 기준으로, 해당 Lecture의 예약 가능 여부를 확인하는 커스텀 어노테이션
}
