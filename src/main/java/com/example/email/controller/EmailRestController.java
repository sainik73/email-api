package com.example.email.controller;


import com.example.email.dto.EmailDto;
import com.example.email.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * This is the REST controller class for the email functionality
 * @author Kiran
 * @since 4/30/2021
 */
@RestController
@Validated
@Slf4j
public class EmailRestController {
    @Autowired
    EmailService emailService;

    @PostMapping(value = "/email/send",
            produces = { "application/json" },
            consumes = { "application/json" })
    public ResponseEntity<Void> sendEmail(@Valid @RequestBody EmailDto body, @Valid boolean enrich) {
        if(log.isDebugEnabled())
            log.debug("Entering send email..." + body);

        ResponseEntity<Void> responseEntity = emailService.sendEmail(body, enrich);

        if(log.isInfoEnabled())
            log.info("Done sending mail using RestTemplate in EmailService.Status -> "+
                          responseEntity.getStatusCode());

        //throw new RuntimeException("Server is busy....");
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }


}
