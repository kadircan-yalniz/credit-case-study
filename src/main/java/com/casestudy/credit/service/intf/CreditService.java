package com.casestudy.credit.service.intf;

import com.casestudy.credit.dto.CreditDTO;
import com.casestudy.credit.dto.GetFilteredCreditsDTO;
import com.casestudy.credit.dto.OpenCreditDTO;
import com.casestudy.credit.exception.ServiceException;

import java.util.List;

public interface CreditService {
    CreditDTO openCredit(OpenCreditDTO createCreditDTO) throws ServiceException;
    List<CreditDTO> listCredits(Long customerId);
    List<CreditDTO> filterCreditByStatusAndOpenDate(GetFilteredCreditsDTO filteredCreditsDTO);
}
