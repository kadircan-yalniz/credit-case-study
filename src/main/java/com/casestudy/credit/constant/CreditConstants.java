package com.casestudy.credit.constant;

import java.math.BigDecimal;

public class CreditConstants {

    private CreditConstants(){ throw new IllegalStateException("Constant Class");}

    public static final Boolean OPEN_CREDIT = Boolean.TRUE;
    public static final Boolean CLOSED_CREDIT = Boolean.FALSE;

    public static final Boolean NOT_PARTIAL = Boolean.FALSE;
    public static final Boolean UNPAID_INSTALLMENT = Boolean.FALSE;
    public static final Boolean PAYED_INSTALLMENT = Boolean.TRUE;

    public static final BigDecimal ANNUAL_FACTOR = BigDecimal.valueOf(360);
    public static final BigDecimal YEARLY_LATENESS_RATE = BigDecimal.valueOf(130);
}
