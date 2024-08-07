package com.banquito.corecobros.companydoc.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PasswordDTO {

    private String user;
    private String oldPassword;
    private String newPassword;
    private String email;

}
