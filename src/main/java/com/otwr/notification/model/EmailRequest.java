package com.otwr.notification.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EmailRequest implements Serializable {

  private static final long serialVersionUID = 1086506076133304786L;
  private String recipientsTo;
  private String recipientsCc;
  private String recipientsBcc;
  private String subject;
  private String templateName;
  private String attachmentName;
  private Object data;
}
