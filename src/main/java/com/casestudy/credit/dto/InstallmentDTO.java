package com.casestudy.credit.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class InstallmentDTO {
    private Long id;
    private BigDecimal installmentAmount;
    private BigDecimal latenessAmount = BigDecimal.ZERO;
    private BigDecimal totalRemainingAmount = BigDecimal.ZERO;
    private Boolean partialFlag;
    private Boolean status;
    private Date installmentDate;
    private Date paymentDate;
}
