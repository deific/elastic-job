package com.myzh.dpc.console.core.controller.support;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myzh.dpc.console.core.exception.BusinessException;
import com.myzh.dpc.console.core.exception.ExceptionCode;
import com.myzh.dpc.console.core.exception.ExceptionFactory;

@ControllerAdvice
public class ControllerExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    @Autowired
    ExceptionFactory exceptionFactory;
    @Autowired
    MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleControllerException(Exception exception) {
        BusinessException businessException;
        if (exception instanceof BindingResult) {
            return onBindError((BindingResult) exception);
        } else if (exception instanceof BusinessException) {
            businessException = (BusinessException) exception;
            logger.error("Business Error occurs:", businessException);
        } else {
            logger.error("Error occurs:", exception);
            businessException = exceptionFactory.create(exception, ExceptionCode.Unknown_Exception);
        }

        exception.printStackTrace();
        ResponseWrapper expWrapper = new ResponseWrapper();
        expWrapper.setRtnCode(businessException.getExpCode());
        expWrapper.setRtnMessage(businessException.getMessage());

        return new ResponseEntity<Object>(expWrapper, HttpStatus.OK);
    }

    private ResponseEntity<?> onBindError(BindingResult bindingResult) {
        FieldError error = bindingResult.getFieldError();
        String code = error.getDefaultMessage();
        String message = messageSource.getMessage(code, error.getArguments(), Locale.CHINA);
        if ("stringEnum".equalsIgnoreCase(error.getCode())) {
            Class enumClass = (Class) error.getArguments()[1];
            message += StringUtils.join(enumClass.getEnumConstants(), ",");
        }
        logger.error("binding parameter error, code={}, message={}", code, message);

        ResponseWrapper wrapper = new ResponseWrapper();
        wrapper.setRtnCode(ExceptionCode.valueOfExpCode(code));
        wrapper.setRtnMessage(message);
        return new ResponseEntity<Object>(wrapper, HttpStatus.OK);
    }
}

