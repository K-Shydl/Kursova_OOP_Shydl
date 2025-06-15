package ua.opnu.pract1shydl.exeptionmy;


public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
