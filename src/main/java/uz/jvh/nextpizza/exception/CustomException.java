package uz.jvh.nextpizza.exception;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

    private final String  errorMessage;
    private final int errorCode;
    private final HttpStatus httpStatus;

    public CustomException(String errorMessage,int errorCode,  HttpStatus httpStatus) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;

    }


    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }


}
