package com.example.email.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * This class represents the inputs for send mail API for SendGrid
 * @author Kiran
 * @since 4/30/2021
 */
@Data
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SendGridMailDto {

    private List<Personalization> personalizations;

    @Valid
    @NotEmpty(message = "Enter valid 'from' email format")
    private EmailAddress from;

    /* Per RFC 2822, subject line should not be more than 78 characters (single line) */
    @NotNull
    @Size(min = 1, max = 78, message= "Subject should be between 1 and 78 characters")
    private String subject;

    @Valid
    private List<Content> content;





}
