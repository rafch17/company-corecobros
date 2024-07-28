package com.banquito.corecobros.companydoc.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CompanyDTO {

    private String uniqueId;
    private String commissionId;
    private String ruc;
    private String companyName;
    private String status;

}
