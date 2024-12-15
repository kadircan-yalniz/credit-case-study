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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.casestudy.credit.constant.CreditConstants.*;
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
        Installment installment = installmentRepository.findById(payInstallmentDTO.getInstallmentId())
                .orElseThrow(()-> new ServiceException(INSTALLMENT_NOT_FOUND));
        InstallmentDTO installmentDTO= calculateInstallment(installment);
        setPayProcessFields(installment, payInstallmentDTO.getAmount(), installmentDTO.getTotalAmount());
        Credit credit = installment.getCredit();
        if(credit.getRemainingAmount().equals(payInstallmentDTO.getAmount().subtract(installmentDTO.getLatenessAmount()))){
            credit.setStatus(CLOSED_CREDIT);
            credit.setCloseDate(DateUtil.getCurrentDate());
        }
        credit.setRemainingAmount(credit.getRemainingAmount().subtract(payInstallmentDTO.getAmount().subtract(installmentDTO.getLatenessAmount())));
        installmentRepository.saveAndFlush(installment);
        creditRepository.saveAndFlush(credit);
    }

    public List<InstallmentDTO> calculateInstallments(Credit credit){
        if(CLOSED_CREDIT.equals(credit.getStatus())){
            return CoreMapper.INSTANCE.toInstallmentDTOList(credit.getInstallments());
        }
        List<Installment> installments = credit.getInstallments();
        return installments.stream().map(installment -> PAYED_INSTALLMENT.equals(installment.getStatus())
                ? CoreMapper.INSTANCE.toInstallmentDTO(installment)
                : calculateInstallment(installment)).collect(Collectors.toList());
    }

    private InstallmentDTO calculateInstallment(Installment installment){
        BigDecimal totalPaidAmount = getTotalPaidAmount(installment);
        BigDecimal remainingAmount = installment.getAmount().subtract(totalPaidAmount);
        BigDecimal latenessAmount = calculateLatenessAmount(installment.getInstallmentDate(), remainingAmount);
        BigDecimal installmentAmount = remainingAmount.add(latenessAmount);
        InstallmentDTO installmentDTO = CoreMapper.INSTANCE.toInstallmentDTO(installment);
        installmentDTO.setLatenessAmount(latenessAmount);
        installmentDTO.setTotalAmount(installmentAmount);
        return installmentDTO;
    }

    private BigDecimal getTotalPaidAmount(Installment installment){
        return installment.getPayments().stream()
                .map(Payment::getPaidAmount)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    private BigDecimal calculateLatenessAmount(Date installmentDate, BigDecimal remainingAmount){
        int lateDays = DateUtil.calculateLateDays(installmentDate);
        BigDecimal latenessFactor = YEARLY_LATENESS_RATE.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return BigDecimal.valueOf(lateDays)
                .multiply(latenessFactor)
                .multiply(remainingAmount)
                .divide(ANNUAL_FACTOR, 2, RoundingMode.HALF_UP);
    }

    private void setPayProcessFields(Installment installment, BigDecimal paymentAmount, BigDecimal totalInstallmentAmount) throws ServiceException {
        validatePaymentAmount(paymentAmount, totalInstallmentAmount);
        Date currentDate = DateUtil.getCurrentDate();
        Payment payment = new Payment();
        payment.setInstallment(installment);
        payment.setPaymentDate(currentDate);
        payment.setPaidAmount(paymentAmount);

        boolean isPartialPayment = paymentAmount.compareTo(totalInstallmentAmount) < 0;
        installment.setPartialFlag(isPartialPayment);
        payment.setPartialFlag(isPartialPayment);
        installment.setStatus(!isPartialPayment);
        if(!isPartialPayment){
            installment.setPaymentDate(currentDate);
            installment.setStatus(PAYED_INSTALLMENT);
        }
        paymentRepository.saveAndFlush(payment);
    }

    private void validatePaymentAmount(BigDecimal paymentAmount, BigDecimal totalInstallmentAmount) throws ServiceException {
        if (paymentAmount.compareTo(totalInstallmentAmount) > 0) {
            throw new ServiceException(PAYMENT_AMOUNT_BIGGER_THAN_TOTAL_AMOUNT);
        }
    }
}
