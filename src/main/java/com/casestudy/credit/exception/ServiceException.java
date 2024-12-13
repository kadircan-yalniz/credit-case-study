package com.casestudy.credit.exception;

import com.casestudy.credit.enumeration.ExceptionType;

import java.io.Serializable;

public class ServiceException extends Exception implements Serializable {
    private static final long serialVersionUID = -1010247499733399300L;
    private final ExceptionType exceptionData;

    public ServiceException(ExceptionType exceptionData) {
        super(exceptionData.getMessage());
        this.exceptionData = exceptionData;
    }

    public Long getErrorCode() {
        return exceptionData.getCode();
    }

    public ExceptionType getExceptionData() {
        return exceptionData;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
