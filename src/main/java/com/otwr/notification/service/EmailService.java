package com.otwr.notification.service;

import com.otwr.notification.model.EmailRequest;

public interface EmailService {

  void sendEmail(EmailRequest emailRequest);

  void sendEmailsByCsv(String templateName, String csvFileName);
}
