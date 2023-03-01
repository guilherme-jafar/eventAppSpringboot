package com.tecside.appEvent.controllers;

import com.tecside.appEvent.errors.ErrorMessages;
import com.tecside.appEvent.models.User;
import com.tecside.appEvent.services.UserService;
import com.tecside.appEvent.utils.JwtGeneratorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("api/v1/users")
public class UserController {


    private final UserService userService;
    private final JwtGeneratorInterface jwtGenerator;


    @Autowired
    public UserController(UserService userService, JwtGeneratorInterface jwtGenerator) {
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;

    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {

            User newUser = userService.saveUser(user);


            return new ResponseEntity<>(newUser, HttpStatus.CREATED);


        } catch (DataIntegrityViolationException e) {
            String errorMessage = e.getMessage();
            if (errorMessage.equals(ErrorMessages.WRONG_EMAIL_FORMAT) || errorMessage.equals(ErrorMessages.MISSING_PASSWORD) || errorMessage.equals(ErrorMessages.MISSING_EMAIL)) {
                return new ResponseEntity<>(ErrorMessages.errorJSON(errorMessage), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.EMAIL_ALREADY_EXISTS), HttpStatus.CONFLICT);
            }


        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.GENERAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {

            if (user.getEmail() == null || user.getPassword() == null) {
                throw new BadCredentialsException(ErrorMessages.MISSING_PASSWORD_OR_EMAIL);

            }


            User userData = userService.login(user.getEmail(), user.getPassword());

            return new ResponseEntity<>(jwtGenerator.generateToken(userData), HttpStatus.OK);

        } catch (BadCredentialsException e) {

            return new ResponseEntity<>(ErrorMessages.errorJSON(e.getMessage()), HttpStatus.UNAUTHORIZED);


        } catch (Exception e) {
            //System.out.println(e.getStackTrace());
            return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.GENERAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") String userId) {

        try {

            Optional<User> user = userService.getUserById(userId);

            if (user.isEmpty()) {
                return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.USER_NOT_FOUND), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(user, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.GENERAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") String userId, @RequestBody User user) {


        try {

            User updatedUser = userService.updateUser(userId, user);

            return new ResponseEntity<>(updatedUser, HttpStatus.OK);

        } catch (DataIntegrityViolationException e) {

            HttpStatus httpStatus = (e.getMessage().equals(ErrorMessages.USER_NOT_FOUND)) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

            return new ResponseEntity<>(ErrorMessages.errorJSON(e.getMessage()), httpStatus);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.GENERAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") String userId) {

        try {

            userService.deleteUser(userId);

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } catch (EmptyResultDataAccessException e) {

            return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.USER_NOT_FOUND), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>(ErrorMessages.errorJSON(ErrorMessages.GENERAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
