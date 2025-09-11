package com.example.demo.dto.mapper;

import com.example.demo.dto.EmployeeRequest;
import com.example.demo.dto.EmployeeResponse;
import com.example.demo.entity.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeMapper {

    public static EmployeeResponse toResponse(Employee employee) {
        EmployeeResponse employeeResponse = new EmployeeResponse();
        BeanUtils.copyProperties(employee, employeeResponse);
        return employeeResponse;
    }

    public static List<EmployeeResponse> toResponse(List<Employee> employees) {
        return employees.stream().map(EmployeeMapper::toResponse).toList();
    }

    public static Employee toEntity(EmployeeRequest employeeRequest) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeRequest, employee);
        return employee;
    }

    public static List<Employee> toEntity(List<EmployeeRequest> employeeRequest) {
        return employeeRequest.stream().map(EmployeeMapper::toEntity).toList();
    }

}
