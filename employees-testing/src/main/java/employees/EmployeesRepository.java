package employees;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeesRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findEmployeeByName(String name);

    @Query("select new employees.EmployeeResource(e.id, e.name) from Employee e")
    List<EmployeeResource> findAllResources();
}
