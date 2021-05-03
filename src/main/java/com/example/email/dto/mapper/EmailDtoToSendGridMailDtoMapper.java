package com.example.email.dto.mapper;

import com.example.email.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * This is a POJO mapper class
 * @author Kiran
 * @since 4/30/2021
 */
@Configuration
public class EmailDtoToSendGridMailDtoMapper {
    @Value("${email.from}")
    private String fromEmailAddress;
    @Value("${email.from.name}")
    private String fromEmailAddressName;

    private static final String IS_HTML_EMAIL = "<html>";
    private static final String HTML_TEXT_EMAIL = "text/html";
    private static final String PLAIN_TEXT_EMAIL = "text/plain";

    private Predicate<List<EmailAddress>> notNullPredicate = Objects::nonNull;
    private UnaryOperator<String> determineContentType =
            s -> s.toLowerCase().startsWith(IS_HTML_EMAIL)?HTML_TEXT_EMAIL:PLAIN_TEXT_EMAIL;

    /**
     * This method maps the EmailDto to SendGridMailDto
     * @param emailDto          input payload of email api, Object of EmailDto
     * @return SendGridMailDto  input payload to SendGrid mail api
     */
    public SendGridMailDto map(EmailDto emailDto){
        SendGridMailDto sendGridMailDto = new SendGridMailDto();
        //Map Personalizations
        Personalization personalization = new Personalization();
        personalization.setTo(emailDto.getTo());
        if(notNullPredicate.test(emailDto.getCc())) personalization.setCc(emailDto.getCc());
        if(notNullPredicate.test(emailDto.getBcc())) personalization.setCc(emailDto.getBcc());
        sendGridMailDto.setPersonalizations(Arrays.asList(personalization));

        EmailAddress fromAddress = new EmailAddress(fromEmailAddress);
        fromAddress.setName(fromEmailAddressName);
        sendGridMailDto.setFrom(fromAddress);

        sendGridMailDto.setSubject(emailDto.getSubject());
        //Map Content of email
        Content content = new Content();
        content.setType(determineContentType.apply(emailDto.getBody()));
        content.setValue(emailDto.getBody());
        sendGridMailDto.setContent(Arrays.asList(content));

        return sendGridMailDto;
    }

}
