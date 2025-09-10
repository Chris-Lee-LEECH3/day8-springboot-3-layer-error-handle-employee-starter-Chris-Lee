package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidSalaryEmployeeException;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        return employeeRepository.getEmployees(gender, page, size);
    }

    public Employee getEmployeeById(int id) {
        Employee employee = employeeRepository.getEmployeeById(id);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employee;
    }

    public Employee createEmployee(Employee employee) {
        if (employee.getAge() == null) {
            throw new InvalidAgeEmployeeException("The age of employee must be provided");
        }

        if (employee.getAge() < 18 || employee.getAge() > 65) {
            throw new InvalidAgeEmployeeException("The age of employee less than 18 or greater than 65.");
        }

        if (employee.getAge() >= 30 && (employee.getSalary() == null || employee.getSalary() < 20000)) {
            throw new InvalidSalaryEmployeeException("The salary of employee age greater than or equal to 30 must be at least 20000.");
        }

        return employeeRepository.createEmployee(employee);
    }

    public Employee updateEmployee(@PathVariable int id, @RequestBody Employee updatedEmployee) {
        Employee found = this.getEmployeeById(id);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        return employeeRepository.updateEmployee(id, updatedEmployee);
    }

    public void deleteEmployeeById(int id) {
        Employee found = this.getEmployeeById(id);
        if (found == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found with id: " + id);
        }
        employeeRepository.deleteEmployee(id);
    }

    public void deleteAllEmployees() {
        employeeRepository.deleteAllEmployees();
    }
}
