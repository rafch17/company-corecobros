package com.banquito.corecobros.companydoc.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.banquito.corecobros.companydoc.dto.CompanyDTO;
import com.banquito.corecobros.companydoc.model.Company;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    @Mapping(source = "uniqueId", target = "uniqueId")
    CompanyDTO toDTO(Company company);

    @Mapping(source = "uniqueId", target = "uniqueId")
    Company toPersistence(CompanyDTO companyDTO);

}
