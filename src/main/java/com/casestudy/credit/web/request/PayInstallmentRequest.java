package com.casestudy.credit.web.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayInstallmentRequest {
    private Long installmentId;
    private BigDecimal amount;
}
