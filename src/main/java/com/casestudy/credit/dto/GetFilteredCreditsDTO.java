package com.casestudy.credit.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GetFilteredCreditsDTO {
    private Integer pageIndex = 0;
    private Integer pageSize = 100;
    private Long customerId;
    private Integer status;
    private Date openDate;
}
