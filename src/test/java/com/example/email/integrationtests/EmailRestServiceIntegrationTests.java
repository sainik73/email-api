package com.example.email.integrationtests;

import com.example.email.dto.EmailAddress;
import com.example.email.dto.EmailDto;
import com.example.email.dto.SendGridMailDto;
import com.example.email.dto.mapper.EmailDtoToSendGridMailDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Kiran
 * @since 5/1/2021
 */
@SpringBootTest
public class EmailRestServiceIntegrationTests {

    @Value("${sendgrid.mail.api.uri}")
    private String sendGridURI;
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;
    TestRestTemplate testRestTemplate;


    @Autowired
    EmailDtoToSendGridMailDtoMapper emailDtoToSendGridMailDtoMapper;

    @BeforeEach
    public void init(){
        RestTemplate restTemplate = new RestTemplate();
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        restTemplateBuilder.configure(restTemplate);
        testRestTemplate = new TestRestTemplate(restTemplateBuilder);
    }

    @Disabled(value = "Enable this test only when send mail integration is to be tested with sendgrid api.")
    @Test()
    public void testSendEmail_accepted(){
        //create HttpEntity with authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+sendGridApiKey);

        //create payload
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(Collections.singletonList(new EmailAddress("one@example.com")));
        emailDto.setBody("<Test body>");
        emailDto.setSubject("Test Subject");
        SendGridMailDto sendGridMailDto = emailDtoToSendGridMailDtoMapper.map(emailDto);

        HttpEntity<SendGridMailDto> request =
                new HttpEntity<>(sendGridMailDto, headers);
        ResponseEntity<Void> responseEntity = testRestTemplate.postForEntity(sendGridURI, request, Void.class);
        assertEquals(202,responseEntity.getStatusCodeValue());
    }

    @Test
    public void testUnauthorizedUser(){
        //create HttpEntity with no authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        SendGridMailDto sendGridMailDto = new SendGridMailDto();
        HttpEntity<SendGridMailDto> request =
                new HttpEntity<>(sendGridMailDto, headers);
        ResponseEntity<Void> responseEntity = testRestTemplate.postForEntity(sendGridURI, request, Void.class);
        assertEquals(401,responseEntity.getStatusCodeValue());
    }

    @Test
    public void testBadRequest(){
        //create HttpEntity
        //set authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer "+sendGridApiKey);
        SendGridMailDto sendGridMailDto = new SendGridMailDto();
        HttpEntity<SendGridMailDto> request =
                new HttpEntity<>(sendGridMailDto, headers);
        ResponseEntity<Void> responseEntity = testRestTemplate.postForEntity(sendGridURI, request, Void.class);
        assertEquals(400,responseEntity.getStatusCodeValue());
    }
}
