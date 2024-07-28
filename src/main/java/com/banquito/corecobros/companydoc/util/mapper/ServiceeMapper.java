package com.banquito.corecobros.companydoc.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.banquito.corecobros.companydoc.dto.ServiceeDTO;
import com.banquito.corecobros.companydoc.model.Servicee;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ServiceeMapper {

    @Mapping(source = "uniqueId", target = "uniqueId")
    ServiceeDTO toDTO(Servicee servicee);

    @Mapping(source = "uniqueId", target = "uniqueId")
    Servicee toPersistence(ServiceeDTO serviceeDTO);

}
