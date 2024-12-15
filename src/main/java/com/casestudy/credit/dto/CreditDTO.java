package com.casestudy.credit.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CreditDTO {
    private Long id;
    private Boolean status;
    private BigDecimal remainingPrincipalAmount;
    private Date openDate;
    private List<InstallmentDTO> installments;
}
