package com.casestudy.credit.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PayInstallmentDTO {
    private Long installmentId;
    private BigDecimal amount;
}
