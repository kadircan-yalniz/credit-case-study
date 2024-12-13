package com.casestudy.credit.web.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GetFilteredCreditsRequest {
    private Long customerId;
    private Integer status;
    private Date openDate;
}
