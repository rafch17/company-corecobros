package com.banquito.corecobros.companydoc.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Account {

    private String uniqueId;
    @NotBlank
    private String codeInternalAccount;
    @NotBlank
    private String type;
    private String status;

}
