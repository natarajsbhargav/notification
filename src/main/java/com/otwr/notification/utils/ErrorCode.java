package com.otwr.notification.utils;

public enum ErrorCode {
  INVALID_EMAIL("Email %s is invalid"),
  NULL_ERROR_MESSAGE("Exception message is null"),
  MAIL_CONNECTION_ERROR("Error while Connecting to Mail SMTP server"),
  INVALID_ATTACHMENT("Attachment %s does not exist or is invalid"),
  NULL_RECIPIENT_ADDRESSES("Recipient Addresses cannot be empty"),
  TEMPLATE_NOT_FOUND("Template not found"),
  EMPTY_VALUES("%s cannot be Empty"),
  UNKNOWN_EXCEPTION("Unknown Exception");

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }

  public String getMessage() {
    return this.message;
  }
}
