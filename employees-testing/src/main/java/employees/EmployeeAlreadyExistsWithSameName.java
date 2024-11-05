package employees;

public class EmployeeAlreadyExistsWithSameName extends RuntimeException {

    public EmployeeAlreadyExistsWithSameName(String message) {
        super(message);
    }
}
