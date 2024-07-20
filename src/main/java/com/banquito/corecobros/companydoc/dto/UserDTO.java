package com.banquito.corecobros.companydoc.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDTO {

    private String id;
    private String companyId;
    private String user;
    private String password;
    private String createDate;
    private String role;
    private String status;
    private String lastConnection;
    private int failedAttempt;
    private String userType;

}
