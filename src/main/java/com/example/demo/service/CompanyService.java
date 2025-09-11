package com.example.demo.service;

import com.example.demo.dto.CompanyRequest;
import com.example.demo.dto.CompanyResponse;
import com.example.demo.dto.mapper.CompanyMapper;
import com.example.demo.entity.Company;
import com.example.demo.repository.ICompanyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CompanyService {

    private final ICompanyRepository companyRepository;

    public CompanyService(ICompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<CompanyResponse> getCompanies(Integer page, Integer size) {
        return CompanyMapper.toResponse(getAllCompany(page, size));
    }

    private List<Company> getAllCompany(Integer page, Integer size) {
        if (page == null || size == null) {
            return companyRepository.findAll();
        }
        Pageable pageable = PageRequest.of(page, size);
        return companyRepository.findAll(pageable).getContent();
    }

    public CompanyResponse createCompany(CompanyRequest company) {
        Company newCompany = CompanyMapper.toEntity(company);
        return CompanyMapper.toResponse(companyRepository.save(newCompany));
    }

    public CompanyResponse getCompanyById(int id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        return CompanyMapper.toResponse(company);
    }

    public CompanyResponse updateCompany(@PathVariable int id, @RequestBody CompanyRequest updatedCompanyRequest) {
        CompanyResponse found = getCompanyById(id);
        Company updatedCompany = CompanyMapper.toEntity(updatedCompanyRequest);
        updatedCompany.setId(found.getId());
        return CompanyMapper.toResponse(companyRepository.save(updatedCompany));
    }

    public void deleteCompanyById(int id) {
        CompanyResponse found = getCompanyById(id);
        companyRepository.deleteById(found.getId());
    }

}
