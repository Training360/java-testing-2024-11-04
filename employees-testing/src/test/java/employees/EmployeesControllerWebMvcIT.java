package employees;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = EmployeesController.class)
public class EmployeesControllerWebMvcIT {

    @MockBean
    EmployeesService employeesService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testListEmployees() throws Exception {
        when(employeesService.listEmployees())
                .thenReturn(List.of(
                        new EmployeeResource(1L, "John Doe"),
                        new EmployeeResource(2L, "Jane Doe")
                ));

        var result = mockMvc.perform(get("/api/employees"))
//                .andDo(print());
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", equalTo("John Doe")))
                .andReturn();

        var json = result.getResponse().getContentAsString();
        var employees = objectMapper.readValue(json, new TypeReference<List<EmployeeResource>>() {});
        assertThat(employees)
                .extracting(EmployeeResource::name)
                .containsExactly("John Doe", "Jane Doe");
    }

}
