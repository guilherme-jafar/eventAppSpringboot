package com.tecside.appEvent.controllers;

import com.tecside.appEvent.models.User;
import com.tecside.appEvent.services.UserService;
import com.tecside.appEvent.utils.JwtGeneratorInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;


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
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {

            userService.saveUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);

        } catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>("{error: A user with the same email already exists}", HttpStatus.CONFLICT);


        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>("{error: An error occurred while processing your request. Please try again later}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {

            if (user.getEmail() == null || user.getPassword() == null) {
                throw new UserPrincipalNotFoundException("Email or Password is Empty");

            }


            User userData = userService.getUserByEmailAndPassword(user.getEmail(), user.getPassword());

            if (userData == null) {
                throw new UserPrincipalNotFoundException("Email or Password is invalid");

            }

            return new ResponseEntity<>(jwtGenerator.generateToken(user), HttpStatus.OK);

        } catch (UserPrincipalNotFoundException e) {

            return new ResponseEntity<>("{error: Invalid email or password}", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ResponseEntity<>("{error: An error occurred while processing your request. Please try again later}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {

        try {

            Optional<User> user = userService.getUserById(userId);

            if(user.isEmpty()){
                return new ResponseEntity<>("{error: The user with ID "+userId+" could not be found}", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(user, HttpStatus.OK);

        }catch (Exception e){
            System.out.println(e.toString());
            return new ResponseEntity<>("{error: An error occurred while processing your request. Please try again later}", HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }
//
//    @PutMapping("/{userId}")
//    public ResponseEntity<Void> updateUser(@PathVariable("userId") Long userId, @RequestBody UserDto userDto) {
//        // Logic for updating an existing user
//    }
//
//    @DeleteMapping("/{userId}")
//    public ResponseEntity<Void> deleteUser(@PathVariable("userId") Long userId) {
//        // Logic for deleting an existing user
//    }
}
