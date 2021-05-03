package com.example.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * @author Kiran
 * @since 4/30/2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAddress {
    @NotNull
    @Email(message = "Email should be valid")
    private String email;

    private String name;

    public EmailAddress(@NotNull @Email(message = "Email should be valid") String emailId) {
        this.email = emailId;
    }
}
