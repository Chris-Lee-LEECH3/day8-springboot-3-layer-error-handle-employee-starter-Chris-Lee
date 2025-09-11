package com.example.demo;

import com.example.demo.entity.Employee;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MvcResult createJohnSmith() throws Exception {
        Employee john = new Employee(null, "John Smith", 28, "MALE", 60000.0);
        Gson gson = new Gson();
        String johnStr = gson.toJson(john);
        return mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(johnStr)).andReturn();
    }

    private MvcResult createJaneDoe() throws Exception {
        Employee jane = new Employee(null, "Jane Doe", 22, "FEMALE", 60000.0);
        Gson gson = new Gson();
        String janeStr = gson.toJson(jane);
        return mockMvc.perform(post("/employees").contentType(MediaType.APPLICATION_JSON).content(janeStr)).andReturn();
    }

    @BeforeEach
    void cleanEmployees() throws Exception {
//        jdbcTemplate.execute("TRUNCATE TABLE employees;");
        jdbcTemplate.execute("DELETE FROM employees;");
        jdbcTemplate.execute("ALTER TABLE employees AUTO_INCREMENT=1;");
    }

    @Test
    void should_return_404_when_employee_not_found() throws Exception {
        mockMvc.perform(get("/employees/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_all_employee() throws Exception {
        createJaneDoe();
        createJohnSmith();

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void should_return_employee_when_employee_found() throws Exception {
        createJohnSmith();

        mockMvc.perform(get("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(28))
                .andExpect(jsonPath("$.gender").value("MALE"));
//                .andExpect(jsonPath("$.salary").value(60000.0));
    }

    @Test
    void should_return_male_employee_when_employee_found() throws Exception {
        createJohnSmith();

        mockMvc.perform(get("/employees?gender=male")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Smith"))
                .andExpect(jsonPath("$[0].age").value(28))
                .andExpect(jsonPath("$[0].gender").value("MALE"));
//                .andExpect(jsonPath("$[0].salary").value(60000.0));
    }

    @Test
    void should_create_employee() throws Exception {
        Employee john = new Employee(null, "John Smith", 28, "MALE", 60000.0);
        Gson gson = new Gson();
        String requestBody = gson.toJson(john);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(28))
                .andExpect(jsonPath("$.gender").value("MALE"));
//                .andExpect(jsonPath("$.salary").value(60000));
    }

    @Test
    void should_return_200_with_empty_body_when_no_employee() throws Exception {
        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void should_return_200_with_employee_list() throws Exception {
        createJohnSmith();

        mockMvc.perform(get("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Smith"))
                .andExpect(jsonPath("$[0].age").value(28))
                .andExpect(jsonPath("$[0].gender").value("MALE"));
//                .andExpect(jsonPath("$[0].salary").value(60000.0));
    }

    @Test
    void should_status_204_when_delete_employee() throws Exception {
        String json = createJohnSmith().getResponse().getContentAsString();
        Employee john = new Gson().fromJson(json, Employee.class);
        mockMvc.perform(delete("/employees/" + john.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_status_200_when_update_employee() throws Exception {
        Gson gson = new Gson();
        String json = createJohnSmith().getResponse().getContentAsString();
        Employee existingJohn = gson.fromJson(json, Employee.class);

        Employee updateInfo = new Employee(null, "John Smith", 29, "MALE", 65000.0);
        String requestBody = gson.toJson(updateInfo);

        mockMvc.perform(put("/employees/" + existingJohn.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingJohn.getId()))
                .andExpect(jsonPath("$.age").value(29));
//                .andExpect(jsonPath("$.salary").value(65000.0));
    }

    @Test
    void should_status_200_and_return_paged_employee_list() throws Exception {
        createJohnSmith();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();
        createJaneDoe();

        mockMvc.perform(get("/employees?page=1&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void should_return_active_true_when_create_employee() throws Exception {
        Employee john = new Employee(null, "John Smith", 28, "MALE", 60000.0);
        Gson gson = new Gson();
        String requestBody = gson.toJson(john);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.age").value(28))
                .andExpect(jsonPath("$.gender").value("MALE"))
//                .andExpect(jsonPath("$.salary").value(60000))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void should_update_active_false_when_delete_employee() throws Exception {
        String json = createJohnSmith().getResponse().getContentAsString();
        Employee john = new Gson().fromJson(json, Employee.class);
        mockMvc.perform(delete("/employees/" + john.getId()))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/employees/" + john.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void should_return_400_when_create_employee_with_age_less_than_18() throws Exception {
        Employee john = new Employee(null, "John Smith", 17, "Male", 60000.0);
        Gson gson = new Gson();
        String requestBody = gson.toJson(john);
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_create_employee_with_age_larger_than_65() throws Exception {
        Employee john = new Employee(null, "John Smith", 66, "Male", 60000.0);
        Gson gson = new Gson();
        String requestBody = gson.toJson(john);
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_create_employee_within_age_is_older_than_30_and_salary_below_20000() throws Exception {
        Employee john = new Employee(null, "John Smith", 31, "Male", 19999.9);
        Gson gson = new Gson();
        String requestBody = gson.toJson(john);
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_400_when_update_employee_with_active_false() throws Exception {
        Gson gson = new Gson();
        String json = createJohnSmith().getResponse().getContentAsString();
        Employee john = gson.fromJson(json, Employee.class);

        mockMvc.perform(delete("/employees/" + john.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        john.setSalary(45000);
        String johnStr = gson.toJson(john);

        mockMvc.perform(put("/employees/" + john.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(johnStr))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_return_404_when_get_employee_by_id_not_exist() throws Exception {
        mockMvc.perform(get("/employees/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
