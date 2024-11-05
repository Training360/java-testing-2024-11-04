package employees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class EmployeesApplicationHttpInterfaceIT {

    @LocalServerPort
    private int port;

    @Autowired
    RestClient.Builder restClientBuilder;

    EmployeesClient employeesClient;

    @BeforeEach
    void setupClient() {
        var restClient = restClientBuilder
                .baseUrl("http://localhost:" + port)
                .build();
        var factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient)).build();
        employeesClient = factory.createClient(EmployeesClient.class);
    }

    @Test
    void listEmployees() {
        var employee1 = new EmployeeResource(null, "John Doe " + UUID.randomUUID());
        var employee2 = new EmployeeResource(null, "Jack Doe " + UUID.randomUUID());

        List.of(
                employee1,
                employee2
        ).forEach(employee -> {
            employeesClient.createEmployee(employee);
        });

        var employees = employeesClient.listEmployees();
        assertThat(employees)
                .extracting(EmployeeResource::name)
                .contains(employee1.name(), employee2.name());
    }

    @HttpExchange("/api/employees")
    interface EmployeesClient {

        @GetExchange
        List<EmployeeResource> listEmployees();

        @PostExchange
        EmployeeResource createEmployee(@RequestBody EmployeeResource command);
    }
}
