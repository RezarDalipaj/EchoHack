package de.dlh.lhind.ecohack.exception.handler;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.response.ErrorDto;
import de.dlh.lhind.ecohack.util.constants.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDto> handleNullPointerException(NullPointerException nullException){
        return buildError(nullException.getMessage() == null ? Constants.NOT_FOUND_MESSAGE
                : nullException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> handleBadRequestException(BadRequestException badRequestException){
        return getError(badRequestException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorDto> handleUnauthorizedException(UnAuthorizedException unAuthorizedException){
        return getError(unAuthorizedException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDeniedException(AccessDeniedException accessDeniedException){
        return getError(accessDeniedException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialsException(BadCredentialsException badCredentialsException){
        return getError(badCredentialsException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationException(MethodArgumentNotValidException validException){
        return getError(validException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception exception){
        return getError(exception, getStatusOfException(exception));
    }

    private HttpStatus getStatusOfException(Exception exception){
        var cause = exception.getCause();
        if (cause instanceof NullPointerException)
            return HttpStatus.NOT_FOUND;
        if (cause instanceof UnAuthorizedException)
            return HttpStatus.UNAUTHORIZED;
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private ResponseEntity<ErrorDto> getError(Exception exception, HttpStatus status){
       return buildError(exception.getMessage(), status);
    }

    private ResponseEntity<ErrorDto> buildError(String message, HttpStatus status){
        var error = ErrorDto.builder()
                .message(message)
                .status(status)
                .build();
        return  ResponseEntity.status(error.getStatus().value()).body(error);
    }
}
