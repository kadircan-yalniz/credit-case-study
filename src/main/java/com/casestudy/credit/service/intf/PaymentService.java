package com.casestudy.credit.service.intf;

import com.casestudy.credit.dao.entity.Credit;
import com.casestudy.credit.dto.InstallmentDTO;
import com.casestudy.credit.dto.PayInstallmentDTO;
import com.casestudy.credit.exception.ServiceException;

import java.util.List;

public interface PaymentService {

    void payInstallment(PayInstallmentDTO payInstallmentDTO) throws ServiceException;
    List<InstallmentDTO> calculateInstallments(Credit credit);
}
