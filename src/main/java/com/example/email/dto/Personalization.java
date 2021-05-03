package com.example.email.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Kiran
 * @since 4/30/2021
 */
@Data
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Personalization {
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
}
