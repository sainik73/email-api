package com.example.email.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

/**
 * This class represents the email payload
 * @author Kiran
 * @since 4/30/2021
 */
@Data
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailDto {
    @Valid
    @NotEmpty(message = "Enter valid 'to' email format")
    // Send grid currently supports max size of 1000 items
    @Size(min=1,max=1000)
    List<EmailAddress> to;

    @Valid
    @Size(max=1000)
    List<EmailAddress> cc;

    @Valid
    @Size(max=1000)
    List<EmailAddress> bcc;

    /* Per RFC 2822, subject line should not be more than 78 characters (single line) */
    @NotNull
    @Size(min = 1, max = 78, message= "Subject should be between 1 and 78 characters")
    private String subject;

    @NotNull
    @Size(min = 1, message= "Email body should be greater than 1 character")
    private String body;

}
