package com.example.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Kiran
 * @since 5/2/2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quote {
    private String text;
    private String author;
    private String tag;
}
