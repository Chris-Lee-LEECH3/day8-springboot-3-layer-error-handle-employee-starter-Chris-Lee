package com.example.demo;

import com.example.demo.dto.CompanyRequest;
import com.example.demo.dto.CompanyResponse;
import com.example.demo.entity.Company;
import com.example.demo.repository.ICompanyRepository;
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
    private ICompanyRepository companyRepository;

//    @Test
//    void should_return_companies_when_get_all_companies() {
//        Company company = new Company(null,"Company 1");
//        Company savedCompany = new Company(1,"Company 1");
//        CompanyRequest companyRequest = new CompanyRequest("Company 1");
//
//        when(companyRepository.save(company)).thenReturn(savedCompany);
//        when(companyRepository.findAll()).thenReturn(List.of(savedCompany));
//        companyService.createCompany(companyRequest);
//
//        List<CompanyResponse> companies = companyService.getCompanies(0, 0);
//        assert(companies.size() == 1);
//    }

}
