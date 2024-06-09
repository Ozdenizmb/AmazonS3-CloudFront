package com.amazon.asset.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.io.Serializable;

import static com.amazon.asset.exception.ErrorMessages.DEFAULT_ERROR_MESSAGE;


@Getter
@Setter
public class FileException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String detail;
    @NotNull
    private final HttpStatus status;

    protected FileException(HttpStatus status, Throwable throwable) {
        super(status.name(), throwable);
        this.status = status;
        this.message = throwable.getMessage();
        this.detail = !StringUtils.hasText(throwable.getMessage()) ? throwable.getMessage() : DEFAULT_ERROR_MESSAGE;
    }

    protected FileException(HttpStatus status, String message) {
        super(status.name());
        this.status = status;
        this.message = message;
        this.detail = null;
    }

    protected FileException(HttpStatus status, String message, String errorDetail) {
        super(status.name());
        this.status = status;
        this.message = message;
        this.detail = errorDetail;
    }

    public static FileException withStatusAndThrowable(HttpStatus status, Throwable throwable){
        return new FileException(status, throwable);
    }

    public static FileException withStatusAndMessage(HttpStatus status, String message){
        return new FileException(status, message);
    }

    public static FileException withStatusAndDetails(HttpStatus status, String message, String errorDetail){
        return new FileException(status, message, errorDetail);
    }
}
