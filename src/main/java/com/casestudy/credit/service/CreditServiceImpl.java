package com.casestudy.credit.service;

import com.casestudy.credit.dao.entity.Credit;
import com.casestudy.credit.dao.entity.Customer;
import com.casestudy.credit.dao.entity.Installment;
import com.casestudy.credit.dao.repository.CreditRepository;
import com.casestudy.credit.dao.repository.CustomerRepository;
import com.casestudy.credit.dao.repository.InstallmentRepository;
import com.casestudy.credit.dto.CreditDTO;
import com.casestudy.credit.dto.GetFilteredCreditsDTO;
import com.casestudy.credit.dto.OpenCreditDTO;
import com.casestudy.credit.enumeration.ExceptionType;
import com.casestudy.credit.exception.ServiceException;
import com.casestudy.credit.mapper.CoreMapper;
import com.casestudy.credit.service.intf.CreditService;
import com.casestudy.credit.service.intf.PaymentService;
import com.casestudy.credit.util.DateUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final CustomerRepository customerRepository;
    private final CreditRepository creditRepository;
    private final InstallmentRepository installmentRepository;
    private final PaymentService paymentService;
    @Override
    @Transactional
    public CreditDTO openCredit(OpenCreditDTO createCreditDTO) throws ServiceException {

        BigDecimal installmentAmount = createCreditDTO.getAmount().divide(BigDecimal.valueOf(createCreditDTO.getInstallmentCount()),2, RoundingMode.UNNECESSARY);
        Optional<Customer> customer = customerRepository.findById(createCreditDTO.getCustomerId());
        if(!customer.isPresent()){
            throw new ServiceException(ExceptionType.CUSTOMER_NOT_FOUND);
        }
        Credit credit = new Credit();
        credit.setRemainingAmount(createCreditDTO.getAmount());
        credit.setCustomer(customer.get());
        List<Installment> installmentList = new ArrayList<>();
        for(int i=1;i< createCreditDTO.getInstallmentCount()+1;i++){
            Installment installment = new Installment();
            installment.setAmount(installmentAmount);
            installment.setCredit(credit);
            installment.setInstallmentDate(DateUtil.findBusinessDay(DateUtil.addDays(DateUtil.getCurrentDate(),30*i)));
            installmentList.add(installment);
        }
        credit.setInstallments(installmentList);
        creditRepository.saveAndFlush(credit);
        installmentRepository.saveAllAndFlush(installmentList);
        return CoreMapper.INSTANCE.toCreditDTO(credit);
    }

    @Override
    public List<CreditDTO> listCredits(Long customerId) {
        List<Credit> credits =  creditRepository.findByCustomerId(customerId);
        if(CollectionUtils.isEmpty(credits)){
            return new ArrayList<>();
        }
        return getCreditDTOList(credits);
    }

    public List<CreditDTO> filterCreditByStatusAndOpenDate(GetFilteredCreditsDTO filteredCreditsDTO){
        Pageable pageable = PageRequest.of(filteredCreditsDTO.getPageIndex(), filteredCreditsDTO.getPageSize());
        List<Credit> credits = creditRepository.filterCreditByStatusAndOpenDate(pageable, filteredCreditsDTO.getCustomerId(),
                filteredCreditsDTO.getStatus(), filteredCreditsDTO.getOpenDate()).getContent();
        if(CollectionUtils.isEmpty(credits)){
            return new ArrayList<>();
        }
        return getCreditDTOList(credits);
    }

    private List<CreditDTO> getCreditDTOList(List<Credit> credits){
        List<CreditDTO> creditDTOList = new ArrayList<>();
        for(Credit credit:credits){
            CreditDTO creditDTO = CoreMapper.INSTANCE.toCreditDTO(credit);
            creditDTO.setInstallments(paymentService.calculateInstallments(credit));
            creditDTOList.add(creditDTO);
        }
        return creditDTOList;
    }

}
