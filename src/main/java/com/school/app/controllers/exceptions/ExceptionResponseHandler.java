package com.school.app.controllers.exceptions;

import com.school.app.dto.response.ExceptionResponse;
import com.school.app.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

@ControllerAdvice
@RestController
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public final ResponseEntity<ExceptionResponse> ResourceAlreadyExistsException(ResourceAlreadyExistsException ex, WebRequest req) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                HttpStatus.UNPROCESSABLE_ENTITY.name(),
                ex.getMessage(),
                "ResourceAlreadyExistsException"
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(InvocationTargetException.class)
    public final ResponseEntity<ExceptionResponse> InvocationTargetException(InvocationTargetException ex, WebRequest req) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                HttpStatus.UNPROCESSABLE_ENTITY.name(),
                ex.getMessage(),
                "InvocationTargetException"
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<ExceptionResponse> IllegalArgumentException(IllegalArgumentException ex, WebRequest req) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage(),
                "IllegalArgumentException"
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                ex.getMessage(),
                "ResourceNotFoundException"
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ExceptionResponse> handleExpiredJwtException(ExpiredJwtException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                ex.getMessage(),
                "ExpiredJwtException"
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleMessagingException(MessagingException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                ex.getMessage(),
                "MessagingException"
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotActiveInTheSystemException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotActiveInTheSystemException(UserNotActiveInTheSystemException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                HttpStatus.UNAUTHORIZED.value(),
                "USER_NOT_ACTIVE",
                ex.getMessage(),
                "UserNotActiveInTheSystemException"
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CodeVerificationExpiredException.class)
    public ResponseEntity<ExceptionResponse> handleCodeVerificationExpiredException(CodeVerificationExpiredException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                HttpStatus.UNAUTHORIZED.value(),
                "CODE_VERIFICATION_EXPIRED",
                ex.getMessage(),
                "CodeVerificationExpiredException"
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CodeVerificationIncorrectException.class)
    public ResponseEntity<ExceptionResponse> handleCodeVerificationIncorrectException(CodeVerificationIncorrectException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                HttpStatus.UNAUTHORIZED.value(),
                "CODE_VERIFICATION_INCORRECT",
                ex.getMessage(),
                "CodeVerificationIncorrectException"
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(PhotoUserErrorException.class)
    public ResponseEntity<ExceptionResponse> handlePhotoUserErrorException(PhotoUserErrorException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                "PHOTO_USER_ERROR",
                ex.getMessage(),
                "PhotoUserErrorException"
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserBlockedException.class)
    public ResponseEntity<ExceptionResponse> handleUserBlockedException(UserBlockedException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                "PHOTO_USER_ERROR",
                ex.getMessage(),
                "UserBlockedException"
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserBadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleUserBadCredentialsException(UserBadCredentialsException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                HttpStatus.BAD_REQUEST.value(),
                "PHOTO_USER_ERROR",
                ex.getMessage(),
                "UserBadCredentialsException"
        );
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
