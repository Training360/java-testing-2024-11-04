package employees;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeesServiceTest {

    @Mock
    EmployeesRepository employeesRepository;

    @Spy
    EmployeeMapper employeesMapper = new EmployeeMapperImpl();

    @InjectMocks
    EmployeesService employeesService;

    @Test
    void createEmployee() {


//        doAnswer(invocation -> {
//            Employee employee = invocation.getArgument(0);
//            employee.setId(66L);
//            return null;
//        }).when(employeesRepository).save(any());

        when(employeesRepository.save(any())).thenReturn(new Employee(66L, "John Doe"));

        var resource = new EmployeeResource(null, "John Doe");
        var result = employeesService.createEmployee(resource);

        assertEquals("John Doe", result.name());
        assertEquals(66L, result.id());
    }
}