package com.banquito.corecobros.companydoc.util.mapper;

import com.banquito.corecobros.companydoc.dto.AccountDTO;
import com.banquito.corecobros.companydoc.dto.CompanyDTO;
import com.banquito.corecobros.companydoc.dto.ServiceeDTO;
import com.banquito.corecobros.companydoc.model.Account;
import com.banquito.corecobros.companydoc.model.Company;
import com.banquito.corecobros.companydoc.model.Servicee;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-08-05T09:49:18-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class CompanyMapperImpl implements CompanyMapper {

    @Override
    public CompanyDTO toDTO(Company company) {
        if ( company == null ) {
            return null;
        }

        CompanyDTO.CompanyDTOBuilder companyDTO = CompanyDTO.builder();

        companyDTO.uniqueId( company.getUniqueId() );
        companyDTO.commissionId( company.getCommissionId() );
        companyDTO.ruc( company.getRuc() );
        companyDTO.companyName( company.getCompanyName() );
        companyDTO.status( company.getStatus() );
        companyDTO.accounts( accountListToAccountDTOList( company.getAccounts() ) );
        companyDTO.servicees( serviceeListToServiceeDTOList( company.getServicees() ) );

        return companyDTO.build();
    }

    @Override
    public Company toPersistence(CompanyDTO companyDTO) {
        if ( companyDTO == null ) {
            return null;
        }

        Company company = new Company();

        company.setUniqueId( companyDTO.getUniqueId() );
        company.setCommissionId( companyDTO.getCommissionId() );
        company.setRuc( companyDTO.getRuc() );
        company.setCompanyName( companyDTO.getCompanyName() );
        company.setStatus( companyDTO.getStatus() );
        company.setAccounts( accountDTOListToAccountList( companyDTO.getAccounts() ) );
        company.setServicees( serviceeDTOListToServiceeList( companyDTO.getServicees() ) );

        return company;
    }

    protected AccountDTO accountToAccountDTO(Account account) {
        if ( account == null ) {
            return null;
        }

        AccountDTO.AccountDTOBuilder accountDTO = AccountDTO.builder();

        accountDTO.uniqueId( account.getUniqueId() );
        accountDTO.codeInternalAccount( account.getCodeInternalAccount() );
        accountDTO.type( account.getType() );
        accountDTO.status( account.getStatus() );

        return accountDTO.build();
    }

    protected List<AccountDTO> accountListToAccountDTOList(List<Account> list) {
        if ( list == null ) {
            return null;
        }

        List<AccountDTO> list1 = new ArrayList<AccountDTO>( list.size() );
        for ( Account account : list ) {
            list1.add( accountToAccountDTO( account ) );
        }

        return list1;
    }

    protected ServiceeDTO serviceeToServiceeDTO(Servicee servicee) {
        if ( servicee == null ) {
            return null;
        }

        ServiceeDTO.ServiceeDTOBuilder serviceeDTO = ServiceeDTO.builder();

        serviceeDTO.uniqueId( servicee.getUniqueId() );
        serviceeDTO.name( servicee.getName() );
        serviceeDTO.status( servicee.getStatus() );
        serviceeDTO.description( servicee.getDescription() );

        return serviceeDTO.build();
    }

    protected List<ServiceeDTO> serviceeListToServiceeDTOList(List<Servicee> list) {
        if ( list == null ) {
            return null;
        }

        List<ServiceeDTO> list1 = new ArrayList<ServiceeDTO>( list.size() );
        for ( Servicee servicee : list ) {
            list1.add( serviceeToServiceeDTO( servicee ) );
        }

        return list1;
    }

    protected Account accountDTOToAccount(AccountDTO accountDTO) {
        if ( accountDTO == null ) {
            return null;
        }

        Account account = new Account();

        account.setUniqueId( accountDTO.getUniqueId() );
        account.setCodeInternalAccount( accountDTO.getCodeInternalAccount() );
        account.setType( accountDTO.getType() );
        account.setStatus( accountDTO.getStatus() );

        return account;
    }

    protected List<Account> accountDTOListToAccountList(List<AccountDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Account> list1 = new ArrayList<Account>( list.size() );
        for ( AccountDTO accountDTO : list ) {
            list1.add( accountDTOToAccount( accountDTO ) );
        }

        return list1;
    }

    protected Servicee serviceeDTOToServicee(ServiceeDTO serviceeDTO) {
        if ( serviceeDTO == null ) {
            return null;
        }

        Servicee servicee = new Servicee();

        servicee.setUniqueId( serviceeDTO.getUniqueId() );
        servicee.setName( serviceeDTO.getName() );
        servicee.setStatus( serviceeDTO.getStatus() );
        servicee.setDescription( serviceeDTO.getDescription() );

        return servicee;
    }

    protected List<Servicee> serviceeDTOListToServiceeList(List<ServiceeDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Servicee> list1 = new ArrayList<Servicee>( list.size() );
        for ( ServiceeDTO serviceeDTO : list ) {
            list1.add( serviceeDTOToServicee( serviceeDTO ) );
        }

        return list1;
    }
}
