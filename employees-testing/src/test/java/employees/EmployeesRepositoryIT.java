package employees;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = true)
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
class EmployeesRepositoryIT {

    @Autowired
    EmployeesRepository employeesRepository;

//    @Test
    @RepeatedTest(10)
    @Transactional
    @Rollback(false)
//    @Sql(statements = "delete from employees")
    @DataSet("employees.yml")
    void findAll() {
//        var employee1 = new Employee("John Doe " + UUID.randomUUID());
//        employeesRepository.save(employee1);
//
//        var employee2 = new Employee("Jack Doe " + UUID.randomUUID());
//        employeesRepository.save(employee2);

        var employees = employeesRepository.findAll();

        assertThat(employees)
                .hasSize(2)
                .extracting(Employee::getName)
//                .containsExactlyInAnyOrder(employee1.getName(), employee2.getName())
                .containsExactlyInAnyOrder("John Doe", "Jack Doe")
        ;
    }
}
