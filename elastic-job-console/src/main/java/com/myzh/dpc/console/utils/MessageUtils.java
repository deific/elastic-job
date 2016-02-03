package com.myzh.dpc.console.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.myzh.dpc.console.core.exception.ExceptionCode;

@Component
public class MessageUtils {
    @Autowired
    private ApplicationContext appContext;

    public String getMessage(ExceptionCode code, Object[] args) {
        return appContext.getMessage(code.getMsgCode(), args, Locale.CHINA);
    }

    public String getMessage(ExceptionCode code) {
        return getMessage(code, null);
    }

}
