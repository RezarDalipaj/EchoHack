package de.dlh.lhind.ecohack.exception.handler;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.response.ErrorDto;
import de.dlh.lhind.ecohack.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDto> handleNullPointerException(NullPointerException nullPointerException){
        var errorDto = ErrorDto.builder()
                .status(HttpStatus.NOT_FOUND)
                .message(nullPointerException.getMessage() == null ? Constants.NOT_FOUND_MESSAGE : nullPointerException.getMessage())
                .build();
        return  ResponseEntity.status(errorDto.getStatus().value()).body(errorDto);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> handleBadRequestException(BadRequestException badRequestException){
        var errorDto = buildError(badRequestException, HttpStatus.BAD_REQUEST);
        return  ResponseEntity.status(errorDto.getStatus().value()).body(errorDto);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorDto> handleUnauthorizedException(UnAuthorizedException unAuthorizedException){
        var errorDto = buildError(unAuthorizedException, HttpStatus.UNAUTHORIZED);
        return  ResponseEntity.status(errorDto.getStatus().value()).body(errorDto);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleAccessDeniedException(AccessDeniedException accessDeniedException){
        var errorDto = buildError(accessDeniedException, HttpStatus.FORBIDDEN);
        return  ResponseEntity.status(errorDto.getStatus().value()).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception exception){
        var errorDto = buildError(exception, HttpStatus.INTERNAL_SERVER_ERROR);
        return  ResponseEntity.status(errorDto.getStatus().value()).body(errorDto);
    }

    private ErrorDto buildError(Exception exception, HttpStatus status){
        return ErrorDto.builder()
                .message(exception.getMessage())
                .status(status)
                .build();
    }
}
