package com.otwr.notification.service;

import org.springframework.web.multipart.MultipartFile;

import com.otwr.notification.model.EmailRequest;

public interface EmailService {

  void sendEmail(EmailRequest emailRequest);

  void sendEmailsByCsv(String templateName, MultipartFile csvFile);
}
