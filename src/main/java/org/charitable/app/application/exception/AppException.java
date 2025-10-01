package org.charitable.app.application.exception;

public class AppException extends RuntimeException {

    public enum ErrorType {
        BAD_REQUEST,
        UNAUTHORIZED,
        NOT_FOUND,
        CONFLICT,
        INTERNAL
    }

    private final ErrorType type;

    private AppException(ErrorType type, String message) {
        super(message);
        this.type = type;
    }

    public ErrorType getType() {
        return type;
    }

    // ---- Static factories ----
    public static AppException badRequest(String message) {
        return new AppException(ErrorType.BAD_REQUEST, message);
    }

    public static AppException unauthorized(String message) {
        return new AppException(ErrorType.UNAUTHORIZED, message);
    }

    public static AppException notFound(String message) {
        return new AppException(ErrorType.NOT_FOUND, message);
    }

    public static AppException conflict(String message) {
        return new AppException(ErrorType.CONFLICT, message);
    }

    public static AppException internal(String message) {
        return new AppException(ErrorType.INTERNAL, message);
    }
}
