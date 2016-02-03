package com.myzh.dpc.console.core.exception;


public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 3443690040705314458L;
    private ExceptionCode exceptionCode;

    public BusinessException(ExceptionCode exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public BusinessException(ExceptionCode exceptionCode, String message, Throwable throwable) {
        super(message, throwable);
        this.exceptionCode = exceptionCode;
    }

    public int getExpCode() {
        return exceptionCode.getExpCode();
    }
    public String getMsgCode() {
        return exceptionCode.getMsgCode();
    }
}
