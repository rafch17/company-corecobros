package com.banquito.corecobros.companydoc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Servicee {

    private String uniqueId;
    private String name;
    private String status;
    private String description;

}
