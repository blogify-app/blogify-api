package com.blogify.blogapi.endpoint.validator;

import com.blogify.blogapi.model.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class RequestInputValidator {
  public static enum InputType {
    QUERY_PARAMS("Query Parameters"),
    PATH_VARIABLE("Path Variable"),
    REQUEST_BODY("Request Body");

    private final String displayValue;

    InputType(String displayValue) {
      this.displayValue = displayValue;
    }

    public String getValue() {
      return displayValue;
    }
  }

  public void notNullValue(InputType inputType, String paramsName, Object input) {
    if (input == null) {
      throw new BadRequestException(inputType.getValue() + " " + paramsName + " is mandatory");
    }
  }
}
