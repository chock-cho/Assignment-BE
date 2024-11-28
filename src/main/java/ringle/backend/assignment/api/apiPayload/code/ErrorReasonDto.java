package ringle.backend.assignment.api.apiPayload.code;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ErrorReasonDto {
    private String code;
    private String message;
    private Boolean isSuccess;
    private HttpStatus httpStatus;
}
