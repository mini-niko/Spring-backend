package br.minikoapi.infra;

import br.minikoapi.dtos.ExceptionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity threadDuplicateInfo(Exception exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("400", "Data already saved");

        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity thread404(Exception exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity generalThread(Exception exception) {
        String exceptionMessage = exception.getLocalizedMessage();
        String errorMessage = exceptionMessage == null || exceptionMessage.isEmpty() ? exception.getMessage() : exceptionMessage;

        ExceptionDTO exceptionDTO = new ExceptionDTO("500", errorMessage);
        return ResponseEntity.internalServerError().body(exceptionDTO);
    }

}
