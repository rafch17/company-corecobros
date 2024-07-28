package com.banquito.corecobros.companydoc.util.mapper;

import com.banquito.corecobros.companydoc.dto.AccountDTO;
import com.banquito.corecobros.companydoc.model.Account;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-28T01:17:10-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class AccountMapperImpl implements AccountMapper {

    @Override
    public AccountDTO toDTO(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountDTO.AccountDTOBuilder accountDTO = AccountDTO.builder();

        accountDTO.uniqueId( account.getUniqueId() );
        accountDTO.companyId( account.getCompanyId() );
        accountDTO.codeInternalAccount( account.getCodeInternalAccount() );
        accountDTO.type( account.getType() );
        accountDTO.status( account.getStatus() );

        return accountDTO.build();
    }

    @Override
    public Account toPersistence(AccountDTO accountDTO) {
        if ( accountDTO == null ) {
            return null;
        }

        Account account = new Account();

        account.setUniqueId( accountDTO.getUniqueId() );
        account.setCompanyId( accountDTO.getCompanyId() );
        account.setCodeInternalAccount( accountDTO.getCodeInternalAccount() );
        account.setType( accountDTO.getType() );
        account.setStatus( accountDTO.getStatus() );

        return account;
    }
}
