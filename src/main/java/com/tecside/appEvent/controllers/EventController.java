package com.tecside.appEvent.controllers;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.ServletException;
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

        try {
            return new ResponseEntity<>("This is a restricted message", HttpStatus.OK);
        }catch (JWTDecodeException e){
            return new ResponseEntity<>("Unauthorized!!", HttpStatus.UNAUTHORIZED);
        }


    }

}
