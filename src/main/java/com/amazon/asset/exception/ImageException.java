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
public class ImageException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final String detail;
    @NotNull
    private final HttpStatus status;

    protected ImageException(HttpStatus status, Throwable throwable) {
        super(status.name(), throwable);
        this.status = status;
        this.message = throwable.getMessage();
        this.detail = !StringUtils.hasText(throwable.getMessage()) ? throwable.getMessage() : DEFAULT_ERROR_MESSAGE;
    }

    protected ImageException(HttpStatus status, String message) {
        super(status.name());
        this.status = status;
        this.message = message;
        this.detail = null;
    }

    protected ImageException(HttpStatus status, String message, String errorDetail) {
        super(status.name());
        this.status = status;
        this.message = message;
        this.detail = errorDetail;
    }

    public static ImageException withStatusAndThrowable(HttpStatus status, Throwable throwable){
        return new ImageException(status, throwable);
    }

    public static ImageException withStatusAndMessage(HttpStatus status, String message){
        return new ImageException(status, message);
    }

    public static ImageException withStatusAndDetails(HttpStatus status, String message, String errorDetail){
        return new ImageException(status, message, errorDetail);
    }
}
