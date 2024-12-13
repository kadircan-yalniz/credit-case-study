package com.casestudy.credit.service;

import com.casestudy.credit.dao.entity.Credit;
import com.casestudy.credit.dao.entity.Installment;
import com.casestudy.credit.dao.entity.Payment;
import com.casestudy.credit.dao.repository.CreditRepository;
import com.casestudy.credit.dao.repository.InstallmentRepository;
import com.casestudy.credit.dao.repository.PaymentRepository;
import com.casestudy.credit.dto.InstallmentDTO;
import com.casestudy.credit.dto.PayInstallmentDTO;
import com.casestudy.credit.exception.ServiceException;
import com.casestudy.credit.mapper.CoreMapper;
import com.casestudy.credit.service.intf.PaymentService;
import com.casestudy.credit.util.DateUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.casestudy.credit.enumeration.ExceptionType.INSTALLMENT_NOT_FOUND;
import static com.casestudy.credit.enumeration.ExceptionType.PAYMENT_AMOUNT_BIGGER_THAN_TOTAL_AMOUNT;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InstallmentRepository installmentRepository;
    private final CreditRepository creditRepository;
    @Override
    @Transactional
    public void payInstallment(PayInstallmentDTO payInstallmentDTO) throws ServiceException {
        Optional<Installment> installmentOpt = installmentRepository.findById(payInstallmentDTO.getInstallmentId());
        if(!installmentOpt.isPresent()){
            throw new ServiceException(INSTALLMENT_NOT_FOUND);
        }
        Installment installment = installmentOpt.get();
        InstallmentDTO installmentDTO= calculateInstallment(installment);
        setPayProcessFields(installment, payInstallmentDTO.getAmount(), installmentDTO.getTotalAmount());
        Credit credit = installment.getCredit();
        if(credit.getRemainingAmount().equals(payInstallmentDTO.getAmount().subtract(installmentDTO.getLatenessAmount()))){
            credit.setStatus(0);
            credit.setCloseDate(DateUtil.getCurrentDate());
        }
        credit.setRemainingAmount(credit.getRemainingAmount().subtract(payInstallmentDTO.getAmount().subtract(installmentDTO.getLatenessAmount())));
        installmentRepository.saveAndFlush(installment);
        creditRepository.saveAndFlush(credit);
    }

    public List<InstallmentDTO> calculateInstallments(Credit credit){
        if(credit.getStatus()==0){
            return CoreMapper.INSTANCE.toInstallmentDTOList(credit.getInstallments());
        }
        List<Installment> installments = credit.getInstallments();
        List<InstallmentDTO> installmentDTOList = new ArrayList<>();
        for(Installment installment:installments){
            if(installment.getStatus()==1){
                installmentDTOList.add(CoreMapper.INSTANCE.toInstallmentDTO(installment));
                continue;
            }
            installmentDTOList.add(calculateInstallment(installment));
        }
        return installmentDTOList;
    }

    private InstallmentDTO calculateInstallment(Installment installment){
        BigDecimal paidAmount = installment.getPayments().stream().map(Payment::getPaidAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal remainingAmount = installment.getAmount().subtract(paidAmount);
        Integer lateDay = DateUtil.calculateLateDay(installment.getInstallmentDate());
        BigDecimal latenessAmount = BigDecimal.valueOf(lateDay).multiply(BigDecimal.valueOf(1.30).divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP)).multiply(remainingAmount).divide(BigDecimal.valueOf(360),2, RoundingMode.HALF_UP);
        BigDecimal installmentAmount = remainingAmount.add(latenessAmount);
        InstallmentDTO installmentDTO = CoreMapper.INSTANCE.toInstallmentDTO(installment);
        installmentDTO.setLatenessAmount(latenessAmount);
        installmentDTO.setTotalAmount(installmentAmount);
        return installmentDTO;
    }

    private void setPayProcessFields(Installment installment, BigDecimal paymentAmount, BigDecimal totalInstallmentAmount) throws ServiceException {
        Payment payment = new Payment();
        payment.setInstallment(installment);
        payment.setPaymentDate(DateUtil.getCurrentDate());
        payment.setPaidAmount(paymentAmount);
        if(paymentAmount.compareTo(totalInstallmentAmount) > 0){
            throw new ServiceException(PAYMENT_AMOUNT_BIGGER_THAN_TOTAL_AMOUNT);
        }
        if(paymentAmount.compareTo(totalInstallmentAmount) < 0){
            payment.setPartialFlag(1);
            installment.setPartialFlag(1);
        }else{
            installment.setPartialFlag(0);
            installment.setPaymentDate(DateUtil.getCurrentDate());
            installment.setStatus(1);
            payment.setPartialFlag(0);
        }
        paymentRepository.saveAndFlush(payment);
    }
}
