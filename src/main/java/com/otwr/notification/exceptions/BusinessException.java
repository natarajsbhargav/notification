package com.otwr.notification.exceptions;

import com.otwr.notification.utils.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class BusinessException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String errorMessage;

  @Override
  public String getMessage() {
    return errorMessage;
  }
}
