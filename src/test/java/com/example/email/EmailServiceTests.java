package com.example.email;

import com.example.email.dto.EmailAddress;
import com.example.email.dto.EmailDto;
import com.example.email.dto.SendGridMailDto;
import com.example.email.dto.mapper.EmailDtoToSendGridMailDtoMapper;
import com.example.email.service.EmailService;
import com.example.email.service.RandomQuoteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

/**
 * @author Kiran
 * @since 5/1/2021
 */
@SpringBootTest
public class EmailServiceTests {

    @Value("${sendgrid.mail.api.uri}")
    private String sendGridURI;
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    EmailDtoToSendGridMailDtoMapper emailDtoToSendGridMailDtoMapper;
    @Autowired
    private EmailService emailService;
    @Autowired
    RandomQuoteService randomQuoteService;
    private MockRestServiceServer mockServer;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void givenSendMail_shouldReturnAccepted() throws URISyntaxException, JsonProcessingException {
        //create payload
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(Collections.singletonList(new EmailAddress("one@example.com")));
        emailDto.setBody("<Test body>");
        emailDto.setSubject("Test Subject");
        SendGridMailDto sendGridMailDto = emailDtoToSendGridMailDtoMapper.map(emailDto);

        mockServer.expect(ExpectedCount.once(),
                            requestTo(new URI(sendGridURI))).
                            andExpect(method(HttpMethod.POST)).
                            andExpect(header("Authorization", "Bearer "+sendGridApiKey)).
                            andExpect(content().json(objectMapper.writeValueAsString(sendGridMailDto))).
                            andRespond(withStatus(HttpStatus.ACCEPTED).
                            contentType(MediaType.APPLICATION_JSON)
                         );

        ResponseEntity<Void> responseEntity = emailService.sendEmail(emailDto,false);
        mockServer.verify();
        assertEquals(HttpStatus.ACCEPTED,responseEntity.getStatusCode());

    }
}
