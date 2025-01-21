package com.otwr.notification.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.otwr.notification.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class ExceptionHelper {

  private static final Map<String, ErrorCode> MESSAGE_ERRORCODE_MAP = new HashMap<>();

  static {
    MESSAGE_ERRORCODE_MAP.put("Unable to find resource 'templates/", ErrorCode.TEMPLATE_NOT_FOUND);
    MESSAGE_ERRORCODE_MAP.put("No recipient addresses", ErrorCode.NULL_RECIPIENT_ADDRESSES);
  }

  private ExceptionHelper() {
  }

  public static BusinessException checkBusinessException(Exception exception) {

    BusinessException businessException = null;
    switch (exception.getClass().getSimpleName()) {
      case "MailConnectException":
        businessException = BusinessException.builder().errorCode(ErrorCode.MAIL_CONNECTION_ERROR)
            .errorMessage(ErrorCode.MAIL_CONNECTION_ERROR.getMessage()).build();
        break;
      case "BusinessException":
        businessException = (BusinessException) exception;
        break;
    }
    if (businessException == null) {
      String errorMessage = exception.getMessage();
      businessException = checkBusinessExceptionWithErrorMessage(errorMessage);
    }
    return businessException;
  }

  private static BusinessException checkBusinessExceptionWithErrorMessage(String errorMessage) {
    BusinessException businessException;
    if (StringUtils.isBlank(errorMessage)) {
      log.error("Error occurred due to Null Error Message ");
      return BusinessException.builder().errorCode(ErrorCode.NULL_ERROR_MESSAGE)
          .errorMessage(ErrorCode.NULL_ERROR_MESSAGE.getMessage()).build();
    }
    ErrorCode errorCode = ErrorCode.UNKNOWN_EXCEPTION;
    for (Map.Entry<String, ErrorCode> entry : MESSAGE_ERRORCODE_MAP.entrySet()) {
      if (errorMessage.contains(entry.getKey())) {
        errorCode = entry.getValue();
        break;
      }
    }
    businessException = BusinessException.builder().errorCode(errorCode).errorMessage(errorCode.getMessage()).build();
    return businessException;
  }
}
