package com.example.demo.dto.mapper;

import com.example.demo.dto.CompanyRequest;
import com.example.demo.dto.CompanyResponse;
import com.example.demo.dto.EmployeeResponse;
import com.example.demo.entity.Company;
import com.example.demo.entity.Employee;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class CompanyMapper {

    public static CompanyResponse toResponse(Company company) {
        CompanyResponse companyResponse = new CompanyResponse();
        BeanUtils.copyProperties(company, companyResponse);
        List<Employee> employees = company.getEmployees();
        List<EmployeeResponse> employeesResponse = new ArrayList<>();

        if (employees != null) {
            if (!employees.isEmpty()) {
                employeesResponse = getEmployeesResponse(employees);
            }
        }

        companyResponse.setEmployees(employeesResponse);
        return companyResponse;
    }

    private static List<EmployeeResponse> getEmployeesResponse(List<Employee> employees) {
        List<EmployeeResponse> employeesResponse = new ArrayList<>();
        employees.forEach(employee -> {
            employeesResponse.add(EmployeeMapper.toResponse(employee));
        });
        return employeesResponse;
    }

    public static List<CompanyResponse> toResponse(List<Company> companies) {
        return companies.stream().map(CompanyMapper::toResponse).toList();
    }

    public static Company toEntity(CompanyRequest companyRequest) {
        Company company = new Company();
        BeanUtils.copyProperties(companyRequest, company);
        return company;
    }

}
