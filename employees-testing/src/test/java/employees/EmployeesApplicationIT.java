package employees;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@Import({TestcontainersConfiguration.class})
class EmployeesApplicationIT {

    @Autowired
    EmployeesController employeesController;

    @Test
    void contextLoads() {}

//    @Test
    @RepeatedTest(10)
//    @DirtiesContext
    void saveThenFind() {
        var resource = new EmployeeResource(null, "John Doe " + UUID.randomUUID());
        var created = employeesController.createEmployee(resource);
        var loaded = employeesController.findEmployeeById(created.id());

        assertEquals(resource.name(), loaded.name());
    }


}
