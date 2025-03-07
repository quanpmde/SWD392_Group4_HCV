package com.ojt_Project.OJT_Project_11_21.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    EMAIL_EXISTED(1001, "Email has existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1002, "Username must at least 5 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003, "Password must at least 8 characters", HttpStatus.BAD_REQUEST),
    EMAIL_NOT_EXISTED(1004, "Email has not existed", HttpStatus.NOT_FOUND),
    LOGIN_FAILED(1005, "Login failed. Please check your email or password", HttpStatus.FORBIDDEN),
    PHONE_INVALID(1006, "Number phone format invalid.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1007, "Access Denied", HttpStatus.FORBIDDEN),
    UNAUTHENTICATED(1008,"Unauthenticated", HttpStatus.UNAUTHORIZED),
    PASSWORD_NOT_MATCHER(1009,"Please enter correct old password", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MATCH(1010,"Do not match. Please enter correct password", HttpStatus.BAD_REQUEST),
    OTP_INVALID(1011,"OTP invalid. Try again" ,HttpStatus.BAD_REQUEST ),
    USER_IS_BANNED(1012,"User is banned.",HttpStatus.BAD_REQUEST),
    SUBJECT_NOT_EXISTED(1013,"SUBJECT_NOT_EXISTED",HttpStatus.BAD_REQUEST),
    SUBJECT_ID_MUST_BE_PROVIDED(1014,"SUBJECT_ID_MUST_BE_PROVIDED",HttpStatus.BAD_REQUEST),
    QUESTIONBANK_NOT_EXISTED(1015,"QUESTIONBANK_NOT_EXISTED",HttpStatus.BAD_REQUEST),
    QUESTIONBANK_ID_MUST_BE_PROVIDED(1016,"QUESTIONBANK_ID_MUST_BE_PROVIDED",HttpStatus.BAD_REQUEST),
    QUESTION_NOT_EXISTED(1017,"QUESTION_NOT_EXISTED",HttpStatus.BAD_REQUEST),
    ANSWER_NOT_EXISTED(1017,"ANSWER_NOT_EXISTED",HttpStatus.BAD_REQUEST),
    EXAM_NOT_EXISTED(1017,"EXAM_NOT_EXISTED",HttpStatus.BAD_REQUEST),
    USER_IS_NOT_FOUNDED(1018,"User is not founded.",HttpStatus.BAD_REQUEST),
    QUESTIONBANK_IS_NOT_FOUNDED(1019,"QUESTIONBANK_IS_NOT_FOUNDED",HttpStatus.BAD_REQUEST),
    SELECTED_QUESTIONS_INVALID(1020,"SELECTED_QUESTIONS_INVALID",HttpStatus.BAD_REQUEST),
    OVER_ATTEMPT_LIMIT(1021,"OVER_ATTEMPT_LIMIT",HttpStatus.BAD_REQUEST),
    TEST_NOT_EXISTED(1022,"TEST_NOT_EXISTED",HttpStatus.BAD_REQUEST),
    TEST_ALREADY_SUBMITTED(1023,"TEST_ALREADY_SUBMITTED",HttpStatus.BAD_REQUEST),
    NOT_ENOUGH_QUESTION(1024,"NOT_ENOUGH_QUESTION",HttpStatus.BAD_REQUEST);

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
