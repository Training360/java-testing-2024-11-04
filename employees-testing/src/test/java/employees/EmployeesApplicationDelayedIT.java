package employees;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.Duration;
import java.util.UUID;

import static org.awaitility.Awaitility.await;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class EmployeesApplicationDelayedIT {

    @Autowired
    EmployeesController employeesController;

    @Test
    void saveThenFindById() {
        var name = "John Doe " + UUID.randomUUID();
        var command = new EmployeeResource(null, name);
        employeesController.createEmployeeDelayed(command);

        await().atMost(Duration.ofSeconds(5))
                .until(() -> {
                    var loaded = employeesController.listEmployees();
                    return loaded.stream().anyMatch(e -> e.name().equals(name));
                });
    }
}