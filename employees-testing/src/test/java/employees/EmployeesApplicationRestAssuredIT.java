package employees;

import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.with;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfiguration.class)
class EmployeesApplicationRestAssuredIT {

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void init() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssuredMockMvc.requestSpecification =
                given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON);
    }

    @Test
    void testListEmployees() {
        var employee1 = new EmployeeResource(null, "John Doe " + UUID.randomUUID());
        var employee2 = new EmployeeResource(null, "Jack Doe " + UUID.randomUUID());
        List.of(
                employee1,
                employee2
        ).forEach(employee -> {
            with()
                    .body(employee)
                    .post("/api/employees")
                    .then()
                    .statusCode(201);
                });

        var employees = with()
                .get("/api/employees")
                .then()
                .statusCode(200)
                .extract().body().as(new TypeRef<List<EmployeeResource>>() {});

        assertThat(employees)
                .extracting(EmployeeResource::name)
                .contains(employee1.name(), employee2.name());
    }

}
