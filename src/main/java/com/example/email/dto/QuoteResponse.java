package com.example.email.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * This class maps to quote service api response
 * Sample Response:
 * {
 *      "status": 200,
 *      "message": "success",
 *      "count": 1,
 *      "quotes": [
 *          {
 *              "text": "We are a big country, with lots of advantages and history.
 *              We are proud to be French. We have to call on patriotism at this time...
 *              to ask for an effort in the battle against debt.",
 *              "author": "Francois Hollande",
 *              "tag": "patriotism"
 *          }
 *      ]
 *  }
 * @author Kiran
 * @since 5/2/2021
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuoteResponse {
    private int httpStatus;
    private String message;
    private int count;
    private List<Quote> quotes;

}
