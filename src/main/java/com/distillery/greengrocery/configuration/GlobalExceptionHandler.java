package com.distillery.greengrocery.configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@Override
    public ResponseEntity<Object>handleMethodArgumentNotValid(
    		MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<FieldError> fieldErrors = ex.getFieldErrors();

        List<Map<String,String>> fieldErrorsMap = fieldErrors.stream()
                .map(fieldError -> {
                    var fieldErrorObject = new Object() {
                        final String fieldName = fieldError.getField();
                        final String errorMessage = fieldError.getDefaultMessage();
                    };
                    return Map.ofEntries(
                            Map.entry("field", fieldErrorObject.fieldName),
                            Map.entry("message", fieldErrorObject.errorMessage != null
                                    ? fieldErrorObject.errorMessage
                                    : ""));})
                .collect(Collectors.toList());

        Map<String,Object> errorResponsePayload =
                Map.of("errors", fieldErrorsMap);
        
        return ResponseEntity.badRequest().body(errorResponsePayload);
    }
	
	@ExceptionHandler(value = DataIntegrityViolationException.class)
	public ResponseEntity<?> handleDataIntegrityViolationException(
			HttpServletRequest request, DataIntegrityViolationException e) {
		String message = e.getMessage();
		Map<String, String> errorResponsePayload = Map.of("errors", message);
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponsePayload);
	}
	
}
