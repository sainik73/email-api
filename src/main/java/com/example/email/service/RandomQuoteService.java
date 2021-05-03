package com.example.email.service;

import com.example.email.dto.Quote;
import com.example.email.dto.QuoteResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Service class to get random quote from external REST API.
 * In case of any error, default quote will be sent out.
 * @author Kiran
 * @since 5/2/2021
 */
@Service
@Slf4j
public class RandomQuoteService {
    private static final String DEFAULT_QUOTE=
            "It always seems impossible until it is done. - Nelson Mandela";

    @Autowired
    RestTemplate restTemplate;

    @Value("${quote.service.uri}")
    private String quoteServiceURI;

    /**
     * This method will get one random quote of the day from goQuotes service
     * Refer: https://goquotes.docs.apiary.io/
     *
     * @return ResponseEntity
     */
    public ResponseEntity<String> getRandomQuote(){
        String responseValue = DEFAULT_QUOTE;
        ResponseEntity<QuoteResponse> responseEntity =
                restTemplate.getForEntity(quoteServiceURI, QuoteResponse.class);
        if(responseEntity.getStatusCode()== HttpStatus.OK){
            Optional<Quote> optionalQuote = responseEntity.getBody().
                                                           getQuotes().
                                                           stream().findFirst();
            if(optionalQuote.isPresent()) {
                responseValue = optionalQuote.get().getText() + " -" +optionalQuote.get().getAuthor();
            }
            return new ResponseEntity<>(responseValue,HttpStatus.OK);

        }else{
            log.debug("Random Quote service call failed with status: "+
                              responseEntity.getStatusCode()+
                              ", returning Default Quote.");
            return new ResponseEntity<>(responseValue, HttpStatus.OK);
        }
    }//end of getRandomQuote
}//end of class
