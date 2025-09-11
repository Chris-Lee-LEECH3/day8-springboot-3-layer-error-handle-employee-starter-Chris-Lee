package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.exception.UpdateInActiveEmployeeException;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidSalaryEmployeeException;
import com.example.demo.repository.IEmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmployeeService {

    private final IEmployeeRepository employeeRepository;

    public EmployeeService(IEmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        if (gender == null) {
            if (page == null || size == null) {
                return employeeRepository.findAll();
            } else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeRepository.findAll(pageable).getContent();
            }
        } else {
            if (page == null || size == null) {
                return employeeRepository.findEmployeesByGender(gender);
            } else {
                Pageable pageable = PageRequest.of(page - 1, size);
                return employeeRepository.findEmployeesByGender(gender, pageable);
            }
        }
    }

    public Employee getEmployeeById(int id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
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

        employee.setActive(true);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(int id, Employee updatedEmployee) {
        Employee found = this.getEmployeeById(id);
        if (!found.getActive()) {
            throw new UpdateInActiveEmployeeException("Cannot update an inactive employee with id: " + id);
        }
        updatedEmployee.setId(found.getId());
        return employeeRepository.save(updatedEmployee);
    }

    public void deleteEmployeeById(int id) {
        Employee found = this.getEmployeeById(id);
        found.setActive(false);
        employeeRepository.save(found);
    }

}
