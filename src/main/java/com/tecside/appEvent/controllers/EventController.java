package com.tecside.appEvent.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/events")
public class EventController {

    @GetMapping("/unrestricted")
    public ResponseEntity<?> index(){
        return new ResponseEntity<>("Hai this is a normal message..", HttpStatus.OK);
    }

    @GetMapping("/restricted")
    public ResponseEntity<?> getRestrictedMessage(){
        return new ResponseEntity<>("This is a restricted message", HttpStatus.OK);
    }

}
