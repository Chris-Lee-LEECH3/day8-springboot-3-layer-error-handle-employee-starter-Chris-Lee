package com.example.demo.repository;

import com.example.demo.entity.Employee;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class EmployeeRepository {
    private final List<Employee> employees = new ArrayList<>();

    public List<Employee> getEmployees(String gender, Integer page, Integer size) {
        Stream<Employee> stream = employees.stream();
        if (gender != null) {
            stream = stream.filter(employee -> employee.getGender().compareToIgnoreCase(gender) == 0);
        }
        if (page != null && size != null) {
            stream = stream.skip((long) (page - 1) * size).limit(size);
        }
        return stream.toList();
    }

    public Employee getEmployeeById(int id) {
        return employees.stream()
                .filter(employee -> employee.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Employee createEmployee(Employee employee) {
        employee.setId(employees.size() + 1);
        employees.add(employee);
        return employee;
    }

    public Employee updateEmployee(@PathVariable int id, @RequestBody Employee updatedEmployee) {
        Employee found = this.getEmployeeById(id);
        found.setName(updatedEmployee.getName());
        found.setAge(updatedEmployee.getAge());
        found.setGender(updatedEmployee.getGender());
        found.setSalary(updatedEmployee.getSalary());
        int index = employees.indexOf(found);
        return employees.set(index, found);
    }

    public void deleteEmployee(int id) {
        Employee found = this.getEmployeeById(id);
        employees.remove(found);
    }

    public void deleteAllEmployees() {
        employees.clear();
    }
}
