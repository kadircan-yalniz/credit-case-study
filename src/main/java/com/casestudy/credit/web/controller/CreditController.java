package com.casestudy.credit.web.controller;

import com.casestudy.credit.dto.CreditDTO;
import com.casestudy.credit.exception.ServiceException;
import com.casestudy.credit.mapper.CoreMapper;
import com.casestudy.credit.service.intf.CreditService;
import com.casestudy.credit.web.request.GetFilteredCreditsRequest;
import com.casestudy.credit.web.request.ListCreditRequest;
import com.casestudy.credit.web.request.OpenCreditRequest;
import com.casestudy.credit.web.response.FilteredCreditResponse;
import com.casestudy.credit.web.response.ListCreditsResponse;
import com.casestudy.credit.web.response.OpenCreditResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Credit API")
@RequiredArgsConstructor
@RequestMapping(value = "/credit")
public class CreditController {

    private final CreditService creditService;

    @Operation(description = "Open credit")
    @PostMapping(path = "/openCredit")
    public ResponseEntity<OpenCreditResponse> openCredit(@RequestBody @Validated OpenCreditRequest request) throws ServiceException {
        CreditDTO credit = creditService.openCredit(CoreMapper.INSTANCE.toOpenCreditDTO(request));
        OpenCreditResponse openCreditResponse = new OpenCreditResponse();
        openCreditResponse.setCreditId(credit.getId());
        openCreditResponse.setInstallments(credit.getInstallments());
        return new ResponseEntity<>(openCreditResponse, HttpStatus.OK);
    }

    @Operation(description = "List credits")
    @PostMapping(path = "/listCredits")
    public ResponseEntity<ListCreditsResponse> listCredits(@RequestBody @Validated ListCreditRequest request){
        ListCreditsResponse response = new ListCreditsResponse();
        response.setCredits(creditService.listCredits(request.getCustomerId()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(description = "Get filtered credits")
    @PostMapping(path = "/getFilteredCredits")
    public ResponseEntity<FilteredCreditResponse> getFilteredCredits(@RequestParam (required = false) Integer pageIndex, @RequestParam (required = false) Integer pageSize, @RequestBody @Validated GetFilteredCreditsRequest request){
        FilteredCreditResponse response = new FilteredCreditResponse();
        response.setCredits(creditService.filterCreditByStatusAndOpenDate(CoreMapper.INSTANCE.toGetFilteredCreditsDTO(pageIndex, pageSize, request)));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
