package com.myzh.dpc.console.core.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class ExceptionFactory {
    @Autowired
    ApplicationContext appContext;

    public BusinessException create(ExceptionCode code, Object... args) {
        return new BusinessException(code, getMessage(code, args));
    }

    public BusinessException create(Throwable throwable, ExceptionCode code, Object... args) {
        return new BusinessException(code, getMessage(code, args), throwable);
    }

    private String getMessage(ExceptionCode code, Object[] args) {
        return appContext.getMessage(code.getMsgCode(), args, Locale.CHINA);
    }
}
