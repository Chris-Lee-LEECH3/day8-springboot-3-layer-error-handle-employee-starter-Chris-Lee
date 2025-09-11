package com.example.demo;

import com.example.demo.entity.Employee;
import com.example.demo.exception.UpdateInActiveEmployeeException;
import com.example.demo.exception.InvalidAgeEmployeeException;
import com.example.demo.exception.InvalidSalaryEmployeeException;
import com.example.demo.repository.IEmployeeRepository;
import com.example.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private IEmployeeRepository employeeRepository;

    @Test
    public void should_return_throw_exception_when_create_a_employee() {
        Employee  employee = new Employee(null, "Tom", 20, "MALE", 2000.0);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        Employee employeeResult = employeeService.createEmployee(employee);
        assertEquals(employeeResult.getAge(), employee.getAge());
    }

    @Test
    public void should_throw_exception_when_create_a_employee_of_greater_that_65_or_less_than_18() {
        Employee employee = new Employee(null, "Tom", 15, "MALE", 2000.0);
        assertThrows(InvalidAgeEmployeeException.class, () -> employeeService.createEmployee(employee));
    }

    @Test
    public void should_throw_exception_when_create_a_employee_of_greater_or_equal_30_and_salary_below_20000() {
        Employee employee = new Employee(null, "Tom", 30, "MALE", 19999.0);
        assertThrows(InvalidSalaryEmployeeException.class, () -> employeeService.createEmployee(employee));
    }

    @Test
    public void should_return_employee_active_true_when_create_employee() {
        Employee employee = new Employee(null, "Tom", 30, "MALE", 20000.0);
        Employee createdEmployee = new Employee(1, "Tom", 30, "MALE", 20000.0, true);
        when(employeeRepository.save(any(Employee.class))).thenReturn(createdEmployee);
        Employee employeeResult = employeeService.createEmployee(employee);
        assertTrue(employeeResult.getActive());
    }

    @Test
    public void should_return_employee_active_false_when_delete_employee() {
        Employee employee = new Employee(1, "Tom", 30, "Male", 20000.0);
        assertTrue(employee.getActive());
        when(employeeRepository.findById(1)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployeeById(1);

        verify(employeeRepository).save(argThat(e -> !e.getActive()));
    }

    @Test
    public void should_throw_exception_when_update_inactive_employee() {
        Employee deletedEmployee = new Employee(1, "Tom", 30, "MALE", 25000.0, false);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(deletedEmployee));
        assertThrows(UpdateInActiveEmployeeException.class, () -> employeeService.updateEmployee(deletedEmployee.getId(), deletedEmployee));
    }

}
