package com.casestudy.credit.web.response;

import com.casestudy.credit.dto.InstallmentDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OpenCreditResponse {
    private Long creditId;
    private List<InstallmentDTO> installments;
}
