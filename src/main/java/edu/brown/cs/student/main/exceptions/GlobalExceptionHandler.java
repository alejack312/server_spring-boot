package edu.brown.cs.student.main.exceptions;

import edu.brown.cs.student.main.responses.ErrorBadJson;
import edu.brown.cs.student.main.responses.ErrorBadRequest;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/** Global exception handler to handle specific exceptions and return custom responses. */
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles MissingServletRequestParameterException when a required query parameter is missing.
   *
   * @param ex the exception
   * @return ResponseEntity with ErrorBadRequest
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorBadRequest> handleMissingParams(
      MissingServletRequestParameterException ex) {
    String name = ex.getParameterName();
    String message = String.format("Missing required query parameter: %s", name);
    ErrorBadRequest error = new ErrorBadRequest(message);
    return new ResponseEntity<>(error, HttpStatus.OK);
  }

  /**
   * Handles ConstraintViolationException for validation errors like @NotBlank.
   *
   * @param ex the exception
   * @return ResponseEntity with ErrorBadRequest
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorBadJson> handleConstraintViolation(ConstraintViolationException ex) {
    String message = ex.getConstraintViolations().iterator().next().getMessage();
    ErrorBadJson error = new ErrorBadJson(message);
    return new ResponseEntity<>(error, HttpStatus.OK);
  }

  /**
   * Handles generic Exception and returns an internal server error.
   *
   * @param ex the exception
   * @return ResponseEntity with ErrorBadJson
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorBadJson> handleGenericException(Exception ex) {
    ErrorBadJson error = new ErrorBadJson("An unexpected error occurred.");
    return new ResponseEntity<>(error, HttpStatus.OK);
  }
}
