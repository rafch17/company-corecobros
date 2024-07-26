package com.banquito.corecobros.companydoc.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDTO {

    private String companyId;
    private String uniqueID;
    private String firstName;
    private String lastName;
    private String user;
    private String password;
    private String email;
    private String role;
    private String status;
    private String userType;

}
