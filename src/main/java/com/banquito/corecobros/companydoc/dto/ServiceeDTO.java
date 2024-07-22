package com.banquito.corecobros.companydoc.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ServiceeDTO {

    private String id;
    private String uniqueID;
    private String name;
    private String status;
    private String description;

}
