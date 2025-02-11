package com.otwr.notification.utils;

public enum ErrorCode {
  INVALID_EMAIL("Email %s is invalid"),
  EMPTY_CSV_FILE("CSV file does not contain any data"),
  NULL_ERROR_MESSAGE("Exception message is null"),
  MAIL_CONNECTION_ERROR("Error while Connecting to Mail SMTP server"),
  INVALID_ATTACHMENT("Attachment %s does not exist or is invalid"),
  NULL_RECIPIENT_ADDRESSES("Recipient Addresses cannot be empty"),
  TEMPLATE_NOT_FOUND("Template not found"),
  INVALID_TEMPLATE_NAME("Template name cannot be empty"),
  INVALID_CSV_FILE_NAME("CSV File name cannot be empty "),
  INVALID_CSV_FILE("CSV File %s does not exist or is invalid "),
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
