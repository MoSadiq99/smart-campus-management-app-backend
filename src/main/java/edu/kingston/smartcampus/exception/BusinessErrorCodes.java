package edu.kingston.smartcampus.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum BusinessErrorCodes {

    NO_CODE(0, "No code", HttpStatus.NOT_IMPLEMENTED),
    INCORRECT_CURRENT_PASSWORD(300, "Incorrect current password", HttpStatus.BAD_REQUEST),
    NEW_PASSWORDS_DO_NOT_MATCH(301, "New passwords do not match", HttpStatus.BAD_REQUEST),
    ACCOUNT_LOCKED(302, "User account is locked", HttpStatus.FORBIDDEN),
    ACCOUNT_DISABLED(303, "User account is disabled", HttpStatus.FORBIDDEN),
    BAD_CREDENTIALS(304, "Invalid credentials", HttpStatus.UNAUTHORIZED),

    // Defined by me
    INVALID_TOKEN(305, "Invalid token", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(306, "Token expired", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(307, "User not found", HttpStatus.BAD_REQUEST),
    USER_NOT_VERIFIED(308, "User not verified", HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXISTS(309, "User already exists", HttpStatus.CONFLICT),
    USER_ALREADY_VERIFIED(310, "User already verified", HttpStatus.CONFLICT),
    USER_ALREADY_REGISTERED(311, "User already registered", HttpStatus.CONFLICT),
    USER_ALREADY_LOGGED_IN(312, "User already logged in", HttpStatus.CONFLICT),
    USER_NOT_REGISTERED(313, "User not registered", HttpStatus.BAD_REQUEST),
    USER_NOT_LOGGED_IN(314, "User not logged in", HttpStatus.BAD_REQUEST),;

    BusinessErrorCodes(int code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }

    private final int code;
    private final String description;
    private final HttpStatus httpStatus;
}
