package com.keifa.bookease.common.handler;

import com.keifa.bookease.catolog.exception.InvalidServiceDurationException;
import com.keifa.bookease.catolog.exception.ServiceNotActiveException;
import com.keifa.bookease.common.exception.UnauthorizedAccessException;
import com.keifa.bookease.professional.exceptions.DuplicateProfessionalProfileException;
import com.keifa.bookease.professional.exceptions.ProfessionalProfileNotFoundException;
import com.keifa.bookease.user.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());

        problemDetail.setTitle("User not found");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problemDetail.setTitle("Email already exists");
        problemDetail.setDetail("An account white the provided email already exists.");
        problemDetail.setInstance(URI.create("/api/v1/users/me"));

        return problemDetail;
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ProblemDetail handleInvalidPasswordException(InvalidPasswordException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());

        problemDetail.setTitle("Invalid Password");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ProblemDetail handlePasswordMismatchException(PasswordMismatchException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle("Password Mismatch");
        problemDetail.setDetail("The new password and confirm new password do not match.");
        problemDetail.setInstance(URI.create("/api/v1/users/me/password"));

        return problemDetail;
    }

    @ExceptionHandler(UserAlreadyInactiveException.class)
    public ProblemDetail handleUserAlreadyInactiveException(UserAlreadyInactiveException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problemDetail.setTitle("User already inactive");
        problemDetail.setDetail("The user is already inactive.");
        problemDetail.setInstance(URI.create("/api/v1/users/{id}/deactivate"));

        return problemDetail;
    }

    @ExceptionHandler(UserAlreadyActiveException.class)
    public ProblemDetail handleUserAlreadyActiveException(UserAlreadyActiveException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problemDetail.setTitle("User already active");
        problemDetail.setDetail("The user is already active.");
        problemDetail.setInstance(URI.create("/api/v1/users/{id}/activate"));

        return problemDetail;
    }

    @ExceptionHandler(ProfessionalProfileNotFoundException.class)
    public ProblemDetail handleProfessionalProfileNotFoundException(ProfessionalProfileNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problemDetail.setTitle("Professional profile not found");
        problemDetail.setInstance(URI.create("/api/v1/professionals/{id}"));

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle("Invalid parameter value");
        problemDetail.setDetail("'" + ex.getValue() + "' is not a valid value for '" + ex.getName() + "'");
        problemDetail.setInstance(URI.create("/api/v1/professionals"));

        return problemDetail;
    }

    @ExceptionHandler(DuplicateProfessionalProfileException.class)
    public ProblemDetail handleDuplicateProfessionalProfileException(DuplicateProfessionalProfileException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problemDetail.setTitle("Duplicate professional profile");
        problemDetail.setInstance(URI.create("/api/v1/users/profile"));

        return problemDetail;
    }

    @ExceptionHandler(InvalidServiceDurationException.class)
    public ProblemDetail handleInvalidServiceDurationException(InvalidServiceDurationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle("Invalid service duration");
        problemDetail.setDetail("The service duration must be a multiple of 15 minutes.");
        problemDetail.setInstance(URI.create(""));

        return problemDetail;
    }

    @ExceptionHandler(ServiceNotActiveException.class)
    public ProblemDetail handleServiceNotActiveException(ServiceNotActiveException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problemDetail.setTitle("Service not active or not exists");
        problemDetail.setDetail("The service is not active.");
        problemDetail.setInstance(URI.create(""));

        return problemDetail;
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ProblemDetail handleUnauthorizedAccessException(UnauthorizedAccessException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());

        problemDetail.setTitle("Unauthorized Access");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return problemDetail;
    }
}
