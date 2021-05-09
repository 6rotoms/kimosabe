package kimosabe.api.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@NoArgsConstructor @Getter @Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse {
    private String message;
    private List<String> errors;
    private int status;

    public ExceptionResponse(RuntimeException exception, HttpStatus statusCode) {
        this.message = exception.getMessage();
        this.status = statusCode.value();
    }

    public ExceptionResponse(RuntimeException exception, HttpStatus statusCode, List<String> errors) {
        this.message = exception.getMessage();
        this.status = statusCode.value();
        this.errors = errors;
    }
}
