package com.banquito.corecobros.companydoc.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDTO {

    private String companyId;
    private String uniqueId;
    private String firstName;
    private String lastName;
    private String user;
    private String password;
    private LocalDate createDate;
    private String email;
    private String role;
    private String status;
    private String userType;

}
