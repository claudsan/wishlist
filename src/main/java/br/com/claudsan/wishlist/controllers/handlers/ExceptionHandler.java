package br.com.claudsan.wishlist.controllers.handlers;

import br.com.claudsan.wishlist.dto.ErrorResponse;
import br.com.claudsan.wishlist.exceptions.NotFoundException;
import br.com.claudsan.wishlist.exceptions.QtdeProductsReachedException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;


@Log4j2
@ControllerAdvice
public class ExceptionHandler {

    @JsonIgnoreProperties(value = {"stackTrace"})
    @org.springframework.web.bind.annotation.ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleConstraintViolation(MethodArgumentNotValidException exception) {
        return responseError(BAD_REQUEST, exception);
    }

    @JsonIgnoreProperties(value = {"stackTrace"})
    @org.springframework.web.bind.annotation.ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        return responseError(NOT_FOUND, exception);
    }

    @JsonIgnoreProperties(value = {"stackTrace"})
    @org.springframework.web.bind.annotation.ExceptionHandler({QtdeProductsReachedException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(QtdeProductsReachedException exception) {
        return responseError(NOT_ACCEPTABLE, exception);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException exception) {
        return responseError(INTERNAL_SERVER_ERROR, exception);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleWebExchangeBindException(WebExchangeBindException exception) {
        return ResponseEntity.status(BAD_REQUEST).body(getFriendlyError(exception.getBindingResult()
                .getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", ")), BAD_REQUEST));
    }

    static ResponseEntity<ErrorResponse> responseError(HttpStatus status, Exception exception) {
        return ResponseEntity.status(status).body(getFriendlyError(exception, status));
    }

    static ErrorResponse getFriendlyError(Exception exception, HttpStatus statusCode) {
        log.error(exception.getMessage());
        return ErrorResponse.builder()
                .message(exception.getMessage())
                .statusCode(statusCode.value())
                .status(statusCode)
                .build();
    }

    static ErrorResponse getFriendlyError(String exception, HttpStatus statusCode) {
        log.error(exception);
        return ErrorResponse.builder()
                .message(exception)
                .statusCode(statusCode.value())
                .status(statusCode)
                .build();
    }

}
