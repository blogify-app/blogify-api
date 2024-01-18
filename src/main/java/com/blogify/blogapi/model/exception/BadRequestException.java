package com.blogify.blogapi.model.exception;

import static com.blogify.blogapi.model.exception.ApiException.ExceptionType.CLIENT_EXCEPTION;

public class BadRequestException extends ApiException {
  public BadRequestException(String message) {
    super(CLIENT_EXCEPTION, message);
  }

}
