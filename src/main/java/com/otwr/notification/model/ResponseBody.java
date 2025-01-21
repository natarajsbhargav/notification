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
public class ResponseBody<T extends Serializable> implements Serializable {

  private static final long serialVersionUID = -2099313403101506011L;
  private boolean success;
  private T data;
  private Error error;
}
