package com.banquito.corecobros.companydoc.util.mapper;

import com.banquito.corecobros.companydoc.dto.CompanyDTO;
import com.banquito.corecobros.companydoc.model.Company;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-07-14T16:40:24-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 21.0.3 (Eclipse Adoptium)"
)
@Component
public class CompanyMapperImpl implements CompanyMapper {

    @Override
    public CompanyDTO toDTO(Company company) {
        if ( company == null ) {
            return null;
        }

        CompanyDTO companyDTO = new CompanyDTO();

        companyDTO.setId( company.getId() );
        companyDTO.setRuc( company.getRuc() );
        companyDTO.setCompanyName( company.getCompanyName() );
        companyDTO.setStatus( company.getStatus() );

        return companyDTO;
    }

    @Override
    public Company toPersistence(CompanyDTO companyDTO) {
        if ( companyDTO == null ) {
            return null;
        }

        Company company = new Company();

        company.setId( companyDTO.getId() );
        company.setRuc( companyDTO.getRuc() );
        company.setCompanyName( companyDTO.getCompanyName() );
        company.setStatus( companyDTO.getStatus() );

        return company;
    }
}
