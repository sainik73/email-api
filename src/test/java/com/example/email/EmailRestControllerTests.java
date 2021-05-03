package com.example.email;

import com.example.email.controller.EmailRestController;
import com.example.email.dto.EmailAddress;
import com.example.email.dto.EmailDto;
import com.example.email.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * @author Kiran
 * @since 5/1/2021
 */
@AutoConfigureMockMvc
@ContextConfiguration(classes = {EmailRestController.class, EmailService.class})
@WebMvcTest
public class EmailRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmailService emailService;
    @MockBean
    RestTemplateBuilder restTemplateBuilder;

    @Test
    public void givenSendEmail_shouldReturnAccepted() throws Exception {
        when(emailService.sendEmail(any(EmailDto.class),anyBoolean())).thenReturn(new ResponseEntity<>(HttpStatus.ACCEPTED));
        //create payload
        EmailDto emailDto = new EmailDto();
        emailDto.setTo(Collections.singletonList(new EmailAddress("someone@example.com")));
        emailDto.setBody("<Test body>");
        emailDto.setSubject("Test Subject");

        ResultActions resultActions =
                mockMvc.perform(post("/email/send").
                                    content(writeAsJsonString(emailDto)).
                                    contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isAccepted());
    }

    @Test
    public void givenSendEmail_whenMandatoryParamMissing_shouldReturnBadRequest() throws Exception {
        when(emailService.sendEmail(any(EmailDto.class),anyBoolean())).thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        //create payload
        EmailDto emailDto = new EmailDto();
        emailDto.setBody("<html>Test body</html>");
        emailDto.setSubject("Test Subject");

        ResultActions resultActions =
                mockMvc.perform(post("/email/send").
                                    content(writeAsJsonString(emailDto)).
                                    contentType(MediaType.APPLICATION_JSON));
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    /**
     * Utility method to create JSON string
     * @param obj Object to convert to json
     * @return String
     */
    private static String writeAsJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            System.out.println(jsonContent);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
