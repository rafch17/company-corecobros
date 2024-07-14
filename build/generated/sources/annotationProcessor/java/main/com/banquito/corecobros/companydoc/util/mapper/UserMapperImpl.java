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
    date = "2024-07-14T16:40:26-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setCompanyId( user.getCompanyId() );
        userDTO.setUser( user.getUser() );
        userDTO.setPassword( user.getPassword() );
        if ( user.getCreateDate() != null ) {
            userDTO.setCreateDate( DateTimeFormatter.ISO_LOCAL_DATE.format( user.getCreateDate() ) );
        }
        userDTO.setRole( user.getRole() );
        userDTO.setStatus( user.getStatus() );
        if ( user.getLastConnection() != null ) {
            userDTO.setLastConnection( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( user.getLastConnection() ) );
        }
        userDTO.setFailedAttempt( user.getFailedAttempt() );
        userDTO.setUserType( user.getUserType() );

        return userDTO;
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
