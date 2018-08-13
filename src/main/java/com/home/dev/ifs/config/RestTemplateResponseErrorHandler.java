package com.home.dev.ifs.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Slf4j
@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return httpResponse.getStatusCode().series() == CLIENT_ERROR
                || httpResponse.getStatusCode().series() == SERVER_ERROR;
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        HttpStatus.Series errorSeries = httpResponse.getStatusCode().series();
        if (errorSeries == HttpStatus.Series.SERVER_ERROR) {
            // error handling for 5xx
        } else if (errorSeries == HttpStatus.Series.CLIENT_ERROR) {
            log.error("Error {} occurs when fetching the data", httpResponse.getStatusCode());
            // // error handling for 4xx
        }
    }
}
