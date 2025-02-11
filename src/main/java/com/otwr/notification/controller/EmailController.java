package com.otwr.notification.controller;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.otwr.notification.exceptions.BusinessException;
import com.otwr.notification.model.EmailRequest;
import com.otwr.notification.model.Error;
import com.otwr.notification.model.ResponseBody;
import com.otwr.notification.service.EmailService;
import com.otwr.notification.utils.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("Email")
@RestController
public class EmailController {

  @Autowired
  private EmailService emailService;

  @PostMapping("/sendEmailByTemplate")
  public ResponseBody<String> sendEmail(@RequestBody EmailRequest requestBody) {

    ResponseBody<String> responseBody;
    try {
      emailService.sendEmail(requestBody);
      responseBody = ResponseBody.<String>builder().success(true).data("Email Sent Successfully").error(null).build();
    } catch (BusinessException be) {
      log.error("Error occurred while sending Email - Error: {}", be.getErrorMessage(), be);
      responseBody = ResponseBody.<String>builder().success(false).data(null)
          .error(Error.builder().errorCode(be.getErrorCode()).errorMessage(be.getErrorMessage()).build()).build();
    } catch (Exception e) {
      log.error("Error occurred while sending Email - Error: {}", e.getMessage(), e);
      responseBody = ResponseBody.<String>builder().success(false).data(null).error(
          Error.builder().errorCode(ErrorCode.UNKNOWN_EXCEPTION).errorMessage(ErrorCode.UNKNOWN_EXCEPTION.getMessage())
              .build()).build();
    }
    return responseBody;
  }

  @PostMapping("/sendEmailsByCsv")
  public ResponseBody<String> sendEmailsByCsv(@RequestParam("templateName") String templateName,
      @RequestParam("csvFileName") String csvFileName) {

    ResponseBody<String> responseBody;
    try {
      emailService.sendEmailsByCsv(templateName, csvFileName);
      responseBody = ResponseBody.<String>builder().success(true)
          .data(String.format("Emails Sent Successfully to Csv: %s", csvFileName)).error(null).build();
    } catch (BusinessException be) {
      log.error("Error occurred while sending Email to Csv: {} - Error: {}", csvFileName, be.getErrorMessage(),
          be);
      responseBody = ResponseBody.<String>builder().success(false).data(null)
          .error(Error.builder().errorCode(be.getErrorCode()).errorMessage(be.getErrorMessage()).build()).build();
    } catch (Exception e) {
      log.error("Error occurred while sending Email to Csv: {} - Error: {}", csvFileName, e.getMessage(), e);
      responseBody = ResponseBody.<String>builder().success(false).data(null).error(
          Error.builder().errorCode(ErrorCode.UNKNOWN_EXCEPTION).errorMessage(ErrorCode.UNKNOWN_EXCEPTION.getMessage())
              .build()).build();
    }
    return responseBody;
  }
}
