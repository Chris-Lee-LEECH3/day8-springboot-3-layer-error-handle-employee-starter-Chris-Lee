package com.example.demo.dto;

public class EmployeeResponse {
    private int id;
    private String name;
    private int age;
    private String gender;
    private Boolean active;

    public EmployeeResponse() {}

    public EmployeeResponse(int id, String name, int age, String gender, Boolean active) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.active = active;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
