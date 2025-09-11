package com.example.demo.dto;

import java.util.List;

public class CompanyResponse {
    private int id;
    private String name;
    private List<EmployeeResponse> employees;

    public CompanyResponse() {}

    public CompanyResponse(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public CompanyResponse(int id, String name, List<EmployeeResponse> employees) {
        this.id = id;
        this.name = name;
        this.employees = employees;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EmployeeResponse> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeResponse> employees) {
        this.employees = employees;
    }

}
