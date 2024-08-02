package com.banquito.corecobros.companydoc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Account {

    private String uniqueId;
    private String codeInternalAccount;
    private String type;
    private String status;

}
