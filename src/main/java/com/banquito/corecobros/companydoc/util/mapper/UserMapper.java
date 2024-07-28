package com.banquito.corecobros.companydoc.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.banquito.corecobros.companydoc.dto.UserDTO;
import com.banquito.corecobros.companydoc.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "uniqueId", target = "uniqueId")
    UserDTO toDTO(User user);

    @Mapping(source = "uniqueId", target = "uniqueId")
    User toPersistence(UserDTO userDTO);

}
