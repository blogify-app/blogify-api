package com.blogify.blogapi.model.exception;

import static com.blogify.blogapi.model.exception.ApiException.ExceptionType.CLIENT_EXCEPTION;

public class NotFoundException extends ApiException {
  public NotFoundException(String message) {
    super(CLIENT_EXCEPTION, message);
  }

}
