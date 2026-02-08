package uz.jvh.nextpizza.exception;

import lombok.Getter;
import uz.jvh.nextpizza.enomerator.ErrorCode;

@Getter
public class NextPizzaException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String details;

    public NextPizzaException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.details = null;
    }

    public NextPizzaException(ErrorCode errorCode, String details) {
        super(errorCode.getMessage() + (details != null ? ": " + details : ""));
        this.errorCode = errorCode;
        this.details = details;
    }

    public NextPizzaException(ErrorCode errorCode, String details, Throwable cause) {
        super(errorCode.getMessage() + (details != null ? ": " + details : ""), cause);
        this.errorCode = errorCode;
        this.details = details;
    }
}
