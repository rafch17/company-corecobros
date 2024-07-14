package com.banquito.corecobros.companydoc.util.mapper;

import com.banquito.corecobros.companydoc.dto.AccountDTO;
import com.banquito.corecobros.companydoc.model.Account;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-14T16:40:26-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public AccountDTO toDTO(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountDTO accountDTO = new AccountDTO();

        accountDTO.setId( account.getId() );
        accountDTO.setCompanyId( account.getCompanyId() );
        accountDTO.setCodeUniqueAccount( account.getCodeUniqueAccount() );
        accountDTO.setNumber( account.getNumber() );
        accountDTO.setType( account.getType() );
        accountDTO.setStatus( account.getStatus() );

        return accountDTO;
    }

    @Override
    public Account toPersistence(AccountDTO accountDTO) {
        if ( accountDTO == null ) {
            return null;
        }

        Account account = new Account();

        account.setId( accountDTO.getId() );
        account.setCompanyId( accountDTO.getCompanyId() );
        account.setCodeUniqueAccount( accountDTO.getCodeUniqueAccount() );
        account.setNumber( accountDTO.getNumber() );
        account.setType( accountDTO.getType() );
        account.setStatus( accountDTO.getStatus() );

        return account;
    }
}
