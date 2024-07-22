package com.banquito.corecobros.companydoc.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountDTO {

    private String id;
    private String companyId;
    private String uniqueID;
    private String codeInternalAccount;
    private String type;
    private String status;

}
