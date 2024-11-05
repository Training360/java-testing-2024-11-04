package employees;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@ExtendWith(MockitoExtension.class)
class ControllerTest {

    @Mock
    EmployeesService employeesService;

    @InjectMocks
    EmployeesController employeesController;

    @Test
    void idsNotMatch() {
        var e = assertThrows(ResponseStatusException.class, () ->
        employeesController.updateEmployee(1L, new EmployeeResource(2L, "John Doe")));
        assertEquals("The id in path does not match with id in request body? 1 != 2", e.getReason());
    }
}
