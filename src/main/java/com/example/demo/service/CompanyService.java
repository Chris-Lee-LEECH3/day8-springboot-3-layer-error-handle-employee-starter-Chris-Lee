package com.example.demo.service;

import com.example.demo.dto.CompanyRequest;
import com.example.demo.dto.CompanyResponse;
import com.example.demo.dto.EmployeeRequest;
import com.example.demo.dto.mapper.CompanyMapper;
import com.example.demo.dto.mapper.EmployeeMapper;
import com.example.demo.entity.Company;
import com.example.demo.entity.Employee;
import com.example.demo.repository.ICompanyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CompanyService {

    private final ICompanyRepository companyRepository;

    private final EmployeeService employeeService;

    public CompanyService(ICompanyRepository companyRepository, EmployeeService employeeService) {
        this.companyRepository = companyRepository;
        this.employeeService = employeeService;
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
        List<EmployeeRequest> employeeRequests = company.getEmployees();
        List<Employee> employees = null;
        if (employeeRequests != null) {
            employees = EmployeeMapper.toEntity(employeeRequests);
        }

        Company newCompany = CompanyMapper.toEntity(company);
        newCompany.setEmployees(employees);

        CompanyResponse createdCompany = CompanyMapper.toResponse(companyRepository.save(newCompany));
        if (employees != null && !employees.isEmpty()) {
            employeeService.updateCompanyIdForEmployees(employees, createdCompany.getId());
        }
        return createdCompany;
    }

    public CompanyResponse getCompanyById(int id) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found with id: " + id);
        }
        return CompanyMapper.toResponse(company);
    }

    public CompanyResponse updateCompany(int id, CompanyRequest updatedCompanyRequest) {
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
