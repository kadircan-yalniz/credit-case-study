package com.casestudy.credit.mapper;

import com.casestudy.credit.dao.entity.Credit;
import com.casestudy.credit.dao.entity.Installment;
import com.casestudy.credit.dto.*;
import com.casestudy.credit.web.request.GetFilteredCreditsRequest;
import com.casestudy.credit.web.request.OpenCreditRequest;
import com.casestudy.credit.web.request.PayInstallmentRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", imports = {Arrays.class})
public interface CoreMapper {
    CoreMapper INSTANCE = Mappers.getMapper(CoreMapper.class);

    OpenCreditDTO toOpenCreditDTO(OpenCreditRequest request);

    @Mapping(target = "installments", source = "installments")
    @Mapping(target = "openDate", source = "createdAt")
    @Mapping(target = "id", source = "id")
    @Named(value = "toCreditDTO")
    CreditDTO toCreditDTO(Credit credit);

    @Mapping(target = "pageIndex", source = "pageIndex")
    @Mapping(target = "pageSize", source = "pageSize")
    @Mapping(target = "status", source = "request.status")
    @Mapping(target = "openDate", source = "request.openDate")
    GetFilteredCreditsDTO toGetFilteredCreditsDTO(Integer pageIndex, Integer pageSize, GetFilteredCreditsRequest request);

    PayInstallmentDTO toPayInstallmentDTO(PayInstallmentRequest request);

    @Mapping(target = "installmentAmount", source = "amount")
    @Named(value = "toInstallmentDTO")
   InstallmentDTO toInstallmentDTO(Installment installment);

    @IterableMapping(qualifiedByName = "toInstallmentDTO")
    List<InstallmentDTO> toInstallmentDTOList(List<Installment> installmentList);
}
