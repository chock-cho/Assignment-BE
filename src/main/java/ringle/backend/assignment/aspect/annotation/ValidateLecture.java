package ringle.backend.assignment.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 어노테이션: 메서드에 적용
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 어노테이션 유지
public @interface ValidateLecture {
    // 항상 현재 시각보다 이후에 있는 타임 슬롯들에 대해서만 생성/삭제 가능
}
