package com.clairtonluz.sigmatest.core.config;

import com.clairtonluz.sigmatest.core.exceptions.NoContentException;
import com.clairtonluz.sigmatest.core.exceptions.UnprocessableEntityException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Hidden
@ControllerAdvice
public class GlobalErrorHandler {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    @ExceptionHandler({NoContentException.class})
    public ErrorResponse handleNoContentException(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    @ExceptionHandler({UnprocessableEntityException.class})
    public ErrorResponse handleHttpMessageNotReadableException(Exception ex) {
        return new ErrorResponse(ex.getMessage());
    }

    public record ErrorResponse(String message) {
    }
}
