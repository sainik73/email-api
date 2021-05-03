package com.example.email.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

/**
 * Custom error handler for RestTemplate
 * @author Kiran
 * @since 4/30/2021
 */
@Slf4j
@Component
public class RestTemplateErrorHandler implements ResponseErrorHandler {
    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        boolean errorResult = true;
        if (clientHttpResponse.getStatusCode().is2xxSuccessful()) {
            return false;
        }
        log.debug("Entering hasError in RestTemplateErrorHandler -> "+ errorResult);
        return errorResult;
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        log.debug("Entering handleError in RestTemplateErrorHandler....");
        if (clientHttpResponse.getStatusCode().series() == SERVER_ERROR ||
                clientHttpResponse.getStatusCode().series() == CLIENT_ERROR) {
            log.debug("Error Status code-> "+clientHttpResponse.getRawStatusCode());

            throw new CustomEmailException(
                    clientHttpResponse.getRawStatusCode(),
                    clientHttpResponse.getStatusText());
        }
    }
}
