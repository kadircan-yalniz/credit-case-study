package com.casestudy.credit.web.controller;

import com.casestudy.credit.exception.ServiceException;
import com.casestudy.credit.mapper.CoreMapper;
import com.casestudy.credit.service.intf.PaymentService;
import com.casestudy.credit.web.request.PayInstallmentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Payment API")
@RequiredArgsConstructor
@RequestMapping(value = "/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(description = "Pay installment")
    @PostMapping(path = "/payInstallment")
    public ResponseEntity<Void> payInstallment(@RequestBody @Validated PayInstallmentRequest request) throws ServiceException {
        paymentService.payInstallment(CoreMapper.INSTANCE.toPayInstallmentDTO(request));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
