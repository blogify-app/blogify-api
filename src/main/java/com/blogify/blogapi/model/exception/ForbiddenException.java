package com.blogify.blogapi.model.exception;

import static com.blogify.blogapi.model.exception.ApiException.ExceptionType.CLIENT_EXCEPTION;

public class ForbiddenException extends ApiException {
  public ForbiddenException(String message) {
    super(CLIENT_EXCEPTION, message);
  }

}
