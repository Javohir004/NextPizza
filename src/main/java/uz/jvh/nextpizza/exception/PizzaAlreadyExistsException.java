package uz.jvh.nextpizza.exception;

public class PizzaAlreadyExistsException extends RuntimeException {
    public PizzaAlreadyExistsException(String message) {
        super(message);
    }
}
