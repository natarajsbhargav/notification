package com.otwr.notification.service.impl;

import java.io.File;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.otwr.notification.exceptions.BusinessException;
import com.otwr.notification.model.EmailRequest;
import com.otwr.notification.service.EmailService;
import com.otwr.notification.utils.ErrorCode;
import com.otwr.notification.utils.ExceptionHelper;
import jakarta.mail.Address;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

  @Autowired
  private VelocityEngine velocityEngine;

  @Value("${spring.mail.host:smtp.gmail.com}")
  private String host;

  @Value("${spring.mail.port:587}")
  private int port;

  @Value("${spring.mail.username:user}")
  private String username;

  @Value("${spring.mail.password:pass}")
  private String password;

  @Value("${spring.mail.properties.mail.smtp.auth:true}")
  private boolean authEnabled;

  @Value("${spring.mail.properties.mail.smtp.starttls.enable:true}")
  private String startTTlsEnabled;

  private Session session;

  @PostConstruct
  private void preProcess() {
    Properties properties = new Properties();
    properties.put("mail.smtp.host", host);
    properties.put("mail.smtp.port", port);
    properties.put("mail.smtp.auth", authEnabled);
    properties.put("mail.smtp.starttls.enable", startTTlsEnabled);

    session = Session.getInstance(properties, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });
  }

  private InternetAddress createInternetAddress(String email) {
    try {
      return new InternetAddress(email);
    } catch (AddressException e) {
      log.error("Error occurred due to Invalid Email: {}", email);
      throw BusinessException.builder().errorCode(ErrorCode.INVALID_EMAIL)
          .errorMessage(String.format(ErrorCode.INVALID_EMAIL.getMessage(), email)).build();
    }
  }

  @Override
  public void sendEmail(EmailRequest emailRequest) {
    validateEmailRequest(emailRequest);
    try {
      Message message = new MimeMessage(session);
      message.setRecipients(Message.RecipientType.TO, buildAddresses(emailRequest.getRecipientsTo()));
      message.setRecipients(Message.RecipientType.CC, buildAddresses(emailRequest.getRecipientsCc()));
      message.setRecipients(Message.RecipientType.BCC, buildAddresses(emailRequest.getRecipientsBcc()));
      message.setSubject(emailRequest.getSubject());
      Multipart multipart = new MimeMultipart();
      MimeBodyPart bodyPart = new MimeBodyPart();
      bodyPart.setContent(buildEmailContentWithData(emailRequest.getTemplateName(), emailRequest.getData()),
          "text/html");
      multipart.addBodyPart(bodyPart);
      if (StringUtils.isNotEmpty(emailRequest.getAttachmentName())) {
        File file = new File(emailRequest.getAttachmentName());
        if (file.exists() && file.isFile()) {
          MimeBodyPart attachment = new MimeBodyPart();
          attachment.attachFile(file);
          multipart.addBodyPart(attachment);
        } else {
          log.error("Invalid attachment: {}", emailRequest.getAttachmentName());
          throw BusinessException.builder().errorCode(ErrorCode.INVALID_ATTACHMENT)
              .errorMessage(String.format(ErrorCode.INVALID_ATTACHMENT.getMessage(), emailRequest.getAttachmentName()))
              .build();
        }
      }
      message.setContent(multipart);
      Transport.send(message);
    } catch (Exception e) {
      log.error("Error occurred while sending Email: {} - Error: {}", emailRequest, e.getMessage(), e);
      throw ExceptionHelper.checkBusinessException(e);
    }
  }

  private void validateEmailRequest(EmailRequest emailRequest) {
    Map<String, String> fields = new HashMap<>();
    fields.put("RecipientsTo", emailRequest.getRecipientsTo());
    fields.put("TemplateName", emailRequest.getTemplateName());
    fields.put("Subject", emailRequest.getSubject());
    String emptyFields =
        fields.entrySet().stream().filter(entry -> StringUtils.isBlank(entry.getValue())).map(Map.Entry::getKey)
            .collect(Collectors.joining(", "));
    if (StringUtils.isNotBlank(emptyFields)) {
      log.error("Error occurred due to Empty Values: {}", emptyFields);
      throw BusinessException.builder().errorCode(ErrorCode.EMPTY_VALUES)
          .errorMessage(String.format(ErrorCode.EMPTY_VALUES.getMessage(), emptyFields)).build();
    }
  }

  private String buildEmailContentWithData(String templateName, Object object) {
    VelocityContext velocityContext = new VelocityContext();
    velocityContext.put("data", object);
    Template template = velocityEngine.getTemplate(String.format("templates/%s.vm", templateName.toLowerCase()));
    StringWriter stringWriter = new StringWriter();
    template.merge(velocityContext, stringWriter);
    return stringWriter.toString();
  }

  private Address[] buildAddresses(String recipients) {
    if (StringUtils.isEmpty(recipients)) {
      return new Address[0];
    }
    return Arrays.stream(recipients.split(";")).map(this::createInternetAddress).toArray(InternetAddress[]::new);
  }
}
