package com.blogify.blogapi.model.exception;

import static com.blogify.blogapi.model.exception.ApiException.ExceptionType.SERVER_EXCEPTION;

public class TooManyRequestException extends ApiException {
  public TooManyRequestException(String message) {
    super(SERVER_EXCEPTION, message);
  }
}
