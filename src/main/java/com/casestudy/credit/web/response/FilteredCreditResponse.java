package com.casestudy.credit.web.response;

import com.casestudy.credit.dto.CreditDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FilteredCreditResponse {
    private List<CreditDTO> credits;
}
