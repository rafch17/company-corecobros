package com.banquito.corecobros.companydoc.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountDTO {

    private String companyId;
    private String uniqueId;
    private String codeInternalAccount;
    private String type;
    private String status;

}
