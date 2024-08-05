package com.banquito.corecobros.companydoc.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Servicee {

    private String uniqueId;
    @NotBlank
    private String name;
    private String status;
    private String description;

}
