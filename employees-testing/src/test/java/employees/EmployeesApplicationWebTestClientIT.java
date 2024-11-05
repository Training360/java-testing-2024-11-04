package employees;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class EmployeesApplicationWebTestClientIT {

    @Autowired
    WebTestClient webClient;

    @Test
    void listEmployees() {
        var employee1 = new EmployeeResource(null, "John Doe " + UUID.randomUUID());
        var employee2 = new EmployeeResource(null, "Jack Doe " + UUID.randomUUID());
        List.of(
                employee1,
                employee2
        ).forEach(
                employee -> {
                    webClient
                            .post()
                            .uri("/api/employees")
                            .bodyValue(employee)
                            .exchange()
                            .expectStatus().isCreated();
                }
        );

        webClient
                .get()
                .uri("/api/employees")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeResource.class).value(employees ->
                        assertThat(employees)
                                .extracting(EmployeeResource::name)
                                .contains(employee1.name(), employee2.name())
                        );
    }


}
