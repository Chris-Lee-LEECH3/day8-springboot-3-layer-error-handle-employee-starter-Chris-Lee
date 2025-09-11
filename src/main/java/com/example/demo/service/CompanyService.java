package com.example.demo.service;

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

    public List<Company> getCompanies(Integer page, Integer size) {
        if (page == null || size == null) {
            return companyRepository.findAll();
        }
        Pageable pageable = PageRequest.of(page, size);
        return companyRepository.findAll(pageable).getContent();
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company getCompanyById(int id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        return company;
    }

    public Company updateCompany(@PathVariable int id, @RequestBody Company updatedCompany) {
        Company found = this.getCompanyById(id);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        updatedCompany.setId(id);
        return companyRepository.save(updatedCompany);
    }

    public void deleteCompanyById(int id) {
        Company found = this.getCompanyById(id);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        companyRepository.deleteById(id);
    }

}
