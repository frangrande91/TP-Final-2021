package edu.utn.TPFinal.exceptions;

import edu.utn.TPFinal.exceptions.alreadyExists.AddressAlreadyExistsException;
import edu.utn.TPFinal.exceptions.alreadyExists.RateAlreadyExists;
import edu.utn.TPFinal.exceptions.notFound.*;
import edu.utn.TPFinal.model.responses.Response;
import edu.utn.TPFinal.utils.EntityResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {

        List<String> errors = new ArrayList<String>();

        for (ConstraintViolation violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass()+" "+violation.getMessage());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

        return new ResponseEntity<Object>(apiError, new HttpHeaders(),apiError.getHttpStatus());
    }

    private <E extends Exception> ResponseEntity<Object> ResponseNotFound(E ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(EntityResponse.messageResponse(ex.getMessage()));
    }

    @ExceptionHandler({ErrorLoginException.class})
    public ResponseEntity<Object> handlerErrorLoginException(ErrorLoginException ex,WebRequest request) {
        return ResponseNotFound(ex);
    }

    @ExceptionHandler({BrandNotExistsException.class})
    public ResponseEntity<Object> handlerBrandNotExistsException(BrandNotExistsException ex,WebRequest request) {
        return ResponseNotFound(ex);
    }

    @ExceptionHandler({AddressNotExistsException.class})
    public ResponseEntity<Object> handlerAddressNotExistsException(AddressNotExistsException ex,WebRequest request) {
        return ResponseNotFound(ex);
    }

    @ExceptionHandler({BillNotExistsException.class})
    public ResponseEntity<Object> handlerBillNotExistsException(BillNotExistsException ex,WebRequest request) {
        return ResponseNotFound(ex);
    }

    @ExceptionHandler({MeasurementNotExistsException.class})
    public ResponseEntity<Object> handlerMeasurementNotExistsException(MeasurementNotExistsException ex,WebRequest request) {
        return ResponseNotFound(ex);
    }

    @ExceptionHandler({MeterNotExistsException.class})
    public ResponseEntity<Object> handlerMeterNotExistsException(MeterNotExistsException ex,WebRequest request) {
        return ResponseNotFound(ex);
    }

    @ExceptionHandler({ModelNotExistsException.class})
    public ResponseEntity<Object> handlerModelNotExistsException(BrandNotExistsException ex,WebRequest request) {
        return ResponseNotFound(ex);
    }

    @ExceptionHandler({RateNotExistsException.class})
    public ResponseEntity<Object> handlerRateNotExistsException(RateNotExistsException ex,WebRequest request) {
        return ResponseNotFound(ex);
    }

    @ExceptionHandler({UserNotExistsException.class})
    public ResponseEntity<Object> handlerUserNotExistsException(UserNotExistsException ex,WebRequest request) {
        return ResponseNotFound(ex);
    }

    @ExceptionHandler({ViolationChangeKeyAttributeException.class})
    public ResponseEntity<Object> handlerViolationChangeKeyAttributeException(ViolationChangeKeyAttributeException ex,WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler({RateAlreadyExists.class})
    public ResponseEntity<Object> handlerRateAlreadyExists(RateAlreadyExists ex,WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(EntityResponse.messageResponse(ex.getMessage()));
    }

    @ExceptionHandler({AccessNotAllowedException.class})
    public ResponseEntity<Object> handlerAccessNotAllowedException(AccessNotAllowedException ex,WebRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(EntityResponse.messageResponse(ex.getMessage()));
    }

    @ExceptionHandler({AddressAlreadyExistsException.class})
    public ResponseEntity<Object> handlerAddressAlreadyExists(AddressAlreadyExistsException ex, WebRequest request) {
        System.out.println("TIPO DE EXCEPCION:"+ex.getClass());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(EntityResponse.messageResponse(ex.getMessage()));
    }
}
