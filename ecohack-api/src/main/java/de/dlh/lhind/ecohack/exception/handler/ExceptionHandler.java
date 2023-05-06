package de.dlh.lhind.ecohack.exception.handler;

import de.dlh.lhind.ecohack.exception.custom.BadRequestException;
import de.dlh.lhind.ecohack.exception.custom.UnAuthorizedException;
import de.dlh.lhind.ecohack.model.dto.response.ErrorDto;
import de.dlh.lhind.ecohack.util.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;


@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDto> handleNullPointerException(NullPointerException nullPointerException){
        ErrorDto errorDto = ErrorDto.builder().build();
        if (nullPointerException.getMessage() == null)
            errorDto.setMessage(Constants.NOT_FOUND_MESSAGE);
        else
            errorDto.setMessage(nullPointerException.getMessage());
        errorDto.setStatus(HttpStatus.NOT_FOUND);
        return  ResponseEntity.status(404).body(errorDto);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> handleBadRequestException(BadRequestException badRequestException){
        ErrorDto errorDto = ErrorDto.builder()
                .message(badRequestException.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return  ResponseEntity.status(400).body(errorDto);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorDto> handleUnauthorizedException(UnAuthorizedException unAuthorizedException){
        ErrorDto errorDto = ErrorDto.builder()
                .message(unAuthorizedException.getMessage())
                .status(HttpStatus.UNAUTHORIZED)
                .build();
        return  ResponseEntity.status(401).body(errorDto);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception exception){
        ErrorDto errorDto = ErrorDto.builder()
                .message(exception.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return  ResponseEntity.status(500).body(errorDto);
    }
}
