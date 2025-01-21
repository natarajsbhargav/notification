package com.otwr.notification.model;

import java.io.Serializable;

import com.otwr.notification.utils.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Error implements Serializable {

  private static final long serialVersionUID = 2343442869806713797L;
  private ErrorCode errorCode;
  private String errorMessage;
}
