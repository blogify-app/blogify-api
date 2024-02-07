package com.blogify.blogapi.endpoint.rest;

import com.blogify.blogapi.endpoint.rest.model.Exception;
import com.blogify.blogapi.model.exception.BadRequestException;
import com.blogify.blogapi.model.exception.ForbiddenException;
import com.blogify.blogapi.model.exception.NotFoundException;
import com.blogify.blogapi.model.exception.TooManyRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class InternalToRestExceptionHandler {

  @ExceptionHandler(value = {BadRequestException.class})
  ResponseEntity<Exception> handleBadRequest(BadRequestException e) {
    log.info("Bad request", e);
    return new ResponseEntity<>(toRest(e, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = {ForbiddenException.class})
  ResponseEntity<Exception> handleForbidden(ForbiddenException e) {
    log.info("Forbidden request", e);
    return new ResponseEntity<>(toRest(e, HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(value = {TooManyRequestException.class})
  ResponseEntity<Exception> handleTooManyRequests(TooManyRequestException e) {
    log.info("Too many requests", e);
    return new ResponseEntity<>(
        toRest(e, HttpStatus.TOO_MANY_REQUESTS), HttpStatus.TOO_MANY_REQUESTS);
  }

  @ExceptionHandler(value = {NotFoundException.class})
  ResponseEntity<Exception> handleNotFound(NotFoundException e) {
    log.info("Not found", e);
    return new ResponseEntity<>(toRest(e, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
  }

  //  ResponseEntity<Exception> handleDefault(java.lang.Exception e) {
  //    log.error("Internal error", e);
  //    return new ResponseEntity<>(
  //        toRest(e, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
  //  }

  private Exception toRest(java.lang.Exception e, HttpStatus status) {
    var restException = new Exception();
    restException.setType(status.toString());
    restException.setMessage(e.getMessage());
    return restException;
  }
}
