package employees;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
public class EmployeesApplicationRestTemplateIT {

    @Autowired
    EmployeesService employeesService;

    @Autowired
    TestRestTemplate template;

    @Test
    void listEmployees() {
        var employee1 = new Employee("John Doe " + UUID.randomUUID());
        template.postForObject("/api/employees", employee1, EmployeeResource.class);
        var employee2 = new Employee("Jack Doe " + UUID.randomUUID());
        template.postForObject("/api/employees", employee2, EmployeeResource.class);

        var employees = template.exchange("/api/employees",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<EmployeeResource>>() {})
                .getBody();

        assertThat(employees)
                .extracting(EmployeeResource::name)
                .contains(employee1.getName(), employee2.getName());
    }

}
