package kimosabe.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@NoArgsConstructor @Getter @Setter
public class ExceptionResponse {
    private String message;
    private int status;

    public ExceptionResponse(RuntimeException exception, HttpStatus statusCode) {
        this.message = exception.getMessage();
        this.status = statusCode.value();
    }
}
