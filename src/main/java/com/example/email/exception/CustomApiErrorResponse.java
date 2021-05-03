package com.example.email.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Kiran
 * @since 4/30/2021
 */
@Data
public class CustomApiErrorResponse {
    private HttpStatus httpStatus;
    private LocalDateTime timestamp;
    private List<String> errors;
    private String debugMessage;
    //default no args constructor
    CustomApiErrorResponse(){}

    //static builder class for CustomErrorDto
    public static class CustomApiErrorResponseBuilder{
        private HttpStatus httpStatus;
        private LocalDateTime timestamp;
        private List<String> errors;
        private String debugMessage;


        public HttpStatus getHttpStatus() {
            return httpStatus;
        }

        public CustomApiErrorResponseBuilder setHttpStatus(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public List<String> getErrors() {
            return errors;
        }

        public CustomApiErrorResponseBuilder setErrors(List<String> errors) {
            this.errors = errors;
            return this;
        }

        public String getDebugMessage() {
            return debugMessage;
        }

        public CustomApiErrorResponseBuilder setDebugMessage(String debugMessage) {
            this.debugMessage = debugMessage;
            return this;
        }

        /**
         * This method builds the CustomApiErrorResponse object
         * @return CustomApiErrorResponse
         */
        public CustomApiErrorResponse build(){
            CustomApiErrorResponse response = new CustomApiErrorResponse();
            response.setTimestamp(LocalDateTime.now());
            response.setDebugMessage(this.getDebugMessage());
            response.setErrors(this.getErrors());
            response.setHttpStatus(this.getHttpStatus());
            return response;
        }

    }
}
