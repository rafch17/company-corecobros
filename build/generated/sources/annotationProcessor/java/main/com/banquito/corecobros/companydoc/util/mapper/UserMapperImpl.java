package com.banquito.corecobros.companydoc.util.mapper;

import com.banquito.corecobros.companydoc.dto.UserDTO;
import com.banquito.corecobros.companydoc.model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-20T01:34:45-0500",
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

        userDTO.id( user.getId() );
        userDTO.companyId( user.getCompanyId() );
        userDTO.user( user.getUser() );
        userDTO.password( user.getPassword() );
        if ( user.getCreateDate() != null ) {
            userDTO.createDate( DateTimeFormatter.ISO_LOCAL_DATE.format( user.getCreateDate() ) );
        }
        userDTO.role( user.getRole() );
        userDTO.status( user.getStatus() );
        if ( user.getLastConnection() != null ) {
            userDTO.lastConnection( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( user.getLastConnection() ) );
        }
        userDTO.failedAttempt( user.getFailedAttempt() );
        userDTO.userType( user.getUserType() );

        return userDTO.build();
    }

    @Override
    public User toPersistence(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDTO.getId() );
        user.setCompanyId( userDTO.getCompanyId() );
        user.setUser( userDTO.getUser() );
        user.setPassword( userDTO.getPassword() );
        if ( userDTO.getCreateDate() != null ) {
            user.setCreateDate( LocalDate.parse( userDTO.getCreateDate() ) );
        }
        user.setRole( userDTO.getRole() );
        user.setStatus( userDTO.getStatus() );
        if ( userDTO.getLastConnection() != null ) {
            user.setLastConnection( LocalDateTime.parse( userDTO.getLastConnection() ) );
        }
        user.setFailedAttempt( userDTO.getFailedAttempt() );
        user.setUserType( userDTO.getUserType() );

        return user;
    }
}
