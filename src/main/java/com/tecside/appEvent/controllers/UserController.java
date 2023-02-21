package com.tecside.appEvent.controllers;

import com.tecside.appEvent.models.User;
import com.tecside.appEvent.services.UserService;
import com.tecside.appEvent.utils.JwtGeneratorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.nio.file.attribute.UserPrincipalNotFoundException;



@RestController
@RequestMapping("api/v1/users")
public class UserController {


    private UserService userService;
    private JwtGeneratorInterface jwtGenerator;

    @Autowired
    public UserController(UserService userService, JwtGeneratorInterface jwtGenerator) {
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        try{

            userService.saveUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user){
        try{

            if(user.getEmail() == null || user.getPassword() ==null){
                throw new UserPrincipalNotFoundException("Email or Password is Empty");

            }


            User userData =  userService.getUserByEmailAndPassword(user.getEmail(), user.getPassword());

            if(userData == null){
                throw new UserPrincipalNotFoundException("Email or Password is invalid");

            }

            return new ResponseEntity<>(jwtGenerator.generateToken(user), HttpStatus.OK);

        }catch (UserPrincipalNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
}
