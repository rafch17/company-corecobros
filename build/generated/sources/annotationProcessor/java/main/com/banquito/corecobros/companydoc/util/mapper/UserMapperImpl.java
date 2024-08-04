package com.banquito.corecobros.companydoc.util.mapper;

import com.banquito.corecobros.companydoc.dto.UserDTO;
import com.banquito.corecobros.companydoc.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-04T12:55:58-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.uniqueId( user.getUniqueId() );
        userDTO.companyId( user.getCompanyId() );
        userDTO.firstName( user.getFirstName() );
        userDTO.lastName( user.getLastName() );
        userDTO.user( user.getUser() );
        userDTO.password( user.getPassword() );
        userDTO.createDate( user.getCreateDate() );
        userDTO.email( user.getEmail() );
        userDTO.role( user.getRole() );
        userDTO.status( user.getStatus() );
        userDTO.userType( user.getUserType() );

        return userDTO.build();
    }

    @Override
    public User toPersistence(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setUniqueId( userDTO.getUniqueId() );
        user.setCompanyId( userDTO.getCompanyId() );
        user.setFirstName( userDTO.getFirstName() );
        user.setLastName( userDTO.getLastName() );
        user.setUser( userDTO.getUser() );
        user.setPassword( userDTO.getPassword() );
        user.setCreateDate( userDTO.getCreateDate() );
        user.setEmail( userDTO.getEmail() );
        user.setRole( userDTO.getRole() );
        user.setStatus( userDTO.getStatus() );
        user.setUserType( userDTO.getUserType() );

        return user;
    }
}
