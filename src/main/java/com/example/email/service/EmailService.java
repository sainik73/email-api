package com.example.email.service;

import com.example.email.dto.Content;
import com.example.email.dto.EmailDto;
import com.example.email.dto.SendGridMailDto;
import com.example.email.dto.mapper.EmailDtoToSendGridMailDtoMapper;
import com.example.email.exception.RestTemplateErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;

/**
 * Service class to send emails.
 * It will optionally call Random quote service to include quote in email, if enrich
 * behavior is on.
 * @author Kiran
 * @since 4/30/2021
 */
@Service
@Slf4j
@PropertySource("classpath:apikey.properties")
public class EmailService {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    EmailDtoToSendGridMailDtoMapper emailDtoToSendGridMailDtoMapper;
    @Autowired
    RandomQuoteService randomQuoteService;

    @Value("${sendgrid.mail.api.uri}")
    private String sendGridURI;
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.
                errorHandler(new RestTemplateErrorHandler()).
                setConnectTimeout(Duration.ofMillis(3000)).
                setReadTimeout(Duration.ofMillis(3000)).
                build();
    }

    /**
     * This method is used to send email by invoking sendgrid v3 mail api.
     * sendgrid v3 mail api responses:-
     * 202 - Accepted, if the requested payload is accepted successfully to send mail
     * 401 - Authorization error
     * 403 - Forbidden error
     * 500 - Server error
     * All the above errors are handled by RestTemplateErrorHandler class.
     * @param body  input payload received by service
     * @return ResponseEntity
     */
    public ResponseEntity<Void> sendEmail(EmailDto body, boolean enrich) {
        //set authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+sendGridApiKey);

        //map EmailDto to SendGridMailDto to pass to SendGrid v3 api
        SendGridMailDto sendGridMailDto = emailDtoToSendGridMailDtoMapper.map(body);

        if(log.isDebugEnabled()) {
            log.debug("From email: " + sendGridMailDto.getFrom());
            log.debug("Subject: " + sendGridMailDto.getSubject());
            sendGridMailDto.getContent().forEach(content -> log.debug("Body Type: " + content));
            log.debug("Personalizations: " + sendGridMailDto.getPersonalizations().size());
        }

        //optional feature - enrich the email
        if(enrich) {
            ResponseEntity<String> quoteResponseEntity = randomQuoteService.getRandomQuote();
            log.debug("quoteResponseEntity: "+ quoteResponseEntity.getBody());
            Content content;
            Content mailContent = sendGridMailDto.getContent().
                                                  stream().
                                                  findFirst().
                                                  orElse(new Content());
            if(mailContent.getType().equalsIgnoreCase("text/html")){
                mailContent.setValue(
                        mailContent.getValue().
                                   replaceFirst("<html>","<html><p>" + quoteResponseEntity.getBody() + "</p>")
                        );
            }else{
                mailContent.setValue(quoteResponseEntity.getBody() + " " + mailContent.getValue());
            }
            sendGridMailDto.setContent(Collections.singletonList(mailContent));
        }

        //create HttpEntity
        HttpEntity<SendGridMailDto> request =
                new HttpEntity<>(sendGridMailDto, headers);
        log.debug("Email body input to sendgrid: "+ request.getBody());
        return restTemplate.postForEntity(sendGridURI, request, Void.class);
    }
}
