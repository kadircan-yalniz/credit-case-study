package com.casestudy.credit.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OpenCreditDTO {
    private Long customerId;
    private BigDecimal amount;
    private Integer installmentCount;
}
