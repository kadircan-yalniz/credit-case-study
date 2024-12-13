package com.casestudy.credit.exception;

import com.casestudy.credit.enumeration.ExceptionType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ExceptionData implements Serializable {
    private static long serialVersionUID = 2147698549428041384L;
    private Long errorCode;
    private String errorMessage;
    public ExceptionData() {}
    public ExceptionData(Long errorCode) {
        this.errorCode = errorCode;
    }public ExceptionData(Long errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }public ExceptionData(ExceptionType exceptionData) {
        this.errorCode = exceptionData.getCode();
    }
}
