package com.casestudy.credit.enumeration;

public enum ExceptionType {
    INSTALLMENT_NOT_FOUND(1001L, "Invalid installment id"),
    PAYMENT_AMOUNT_BIGGER_THAN_TOTAL_AMOUNT(1002L, "Payment amount should be smaller than total installment amount."),
    CUSTOMER_NOT_FOUND(1003L, "Customer not found.");

    private final Long code;
    private final String message;
    ExceptionType(Long code, String message) {
        this.code = code;
        this.message = message;
    }
    public Long getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
