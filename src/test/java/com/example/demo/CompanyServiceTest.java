package com.example.demo;

import com.example.demo.entity.Company;
import com.example.demo.repository.CompanyRepository;
import com.example.demo.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTest {

    @InjectMocks
    private CompanyService companyService;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    void should_return_companies_when_get_all_companies() {
        Company company = new Company(null, "Company 1");
        Company createdCompany = new Company(1, "Company 1");
        when(companyRepository.createCompany(company)).thenReturn(createdCompany);
        when(companyRepository.getCompanies(0,0)).thenReturn(List.of(company));

        companyService.createCompany(company);
        List<Company> companies = companyService.getCompanies(0, 0);
        assert(companies.size() == 1);
    }

}
