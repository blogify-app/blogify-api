package com.blogify.blogapi.model.exception;

public class ApiException extends RuntimeException {
  private final ExceptionType type;
  public ApiException(ExceptionType type, String message){
    super(message);
    this.type = type;
  }

  public enum ExceptionType {
    SERVER_EXCEPTION, CLIENT_EXCEPTION
  }
}
