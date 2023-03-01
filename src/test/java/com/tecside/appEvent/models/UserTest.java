package com.tecside.appEvent.models;

import com.tecside.appEvent.errors.ErrorMessages;
import com.tecside.appEvent.repositories.UserRepository;
import com.tecside.appEvent.services.UserService;
import com.tecside.appEvent.services.UserServiceImpl;
import com.tecside.appEvent.utils.JwtGeneratorInterface;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest {


    //@Autowired
    @Autowired
    private UserServiceImpl userService;


    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testLoginSuccess()throws Exception {

        String password = "passe12";
        String email = "john.doe@gmail.com";


        User user = userService.login(email, password);

        assertNotNull(user);


    }

    @Test
    public void testInvalidLogin() throws Exception {

        String password = "pas12";
        String email = "john.doe@gmail.com";


        try {
            User user = userService.login(email, password);
            fail("Expected BadCredentialsException to be thrown");
        } catch (BadCredentialsException e) {
            assertEquals(ErrorMessages.INVALID_PASSWORD_EMAIL, e.getMessage());
        }

    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void testCreateUserMissingEmail() throws Exception {


        String password = "passe12";
        User user = new User();
        user.setPassword(password);

        try {
            userService.saveUser(user);
            fail("Expected DataIntegrityViolationException to be thrown");
        } catch (DataIntegrityViolationException e) {
            assertEquals(ErrorMessages.MISSING_EMAIL, e.getMessage());
        }

    }

    @Test
    public void testCreateUserMissingPassword() throws Exception {


        String email = "john.doe@gmail.com";
        User user = new User();
        user.setEmail(email);


        try {
            userService.saveUser(user);
            fail("Expected DataIntegrityViolationException to be thrown");
        } catch (DataIntegrityViolationException e) {
            assertEquals(ErrorMessages.MISSING_PASSWORD, e.getMessage());
        }

    }

    @Test
    public void testCreateUserWrongEmailFormat() throws Exception {

        String password = "passe12";
        String email = "john.dogmail.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);


        try {
            userService.saveUser(user);
            fail("Expected DataIntegrityViolationException to be thrown");
        } catch (DataIntegrityViolationException e) {
            assertEquals(ErrorMessages.WRONG_EMAIL_FORMAT, e.getMessage());
        }

    }

    @Test
    public void testSuccessfulCreateUser() throws Exception {

        String password = "passe12";
        String email = "john.doe7@gmail.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);



        assertNotNull( userService.saveUser(user));




    }

    @Test
    public void testSuccessfulUpdate() throws Exception {

        User user = new User();
        user.setEmail("john.doe@gmail.com");
        user.setPassword("passe123");
        user.setFullName("John Doe");
        user.setMobileNumber("987456321");
        user.setCountryCode("+244");


        assertNotNull( userService.updateUser("65456e01-1a28-42bf-b217-07506efbd654", user));

    }

    @Test
    public void testUpdateWrongNumberFormat() throws Exception {

        User user = new User();
        user.setEmail("john.doe@gmail.com");
        user.setPassword("passe123");
        user.setFullName("John Doe");
        user.setMobileNumber("9876321");
        user.setCountryCode("+244");

        try {
            assertNotNull( userService.updateUser("65456e01-1a28-42bf-b217-07506efbd654", user));
            fail("Expected DataIntegrityViolationException to be thrown");
        } catch (DataIntegrityViolationException e) {
            assertEquals(ErrorMessages.WRONG_NUMBER_FORMAT, e.getMessage());
        }




    }

    @Test
    public void testUpdateUserNotFound() throws Exception {

        User user = new User();
        user.setEmail("john.doe@gmail.com");
        user.setPassword("passe123");
        user.setFullName("John Doe");
        user.setMobileNumber("987456321");
        user.setCountryCode("+244");

        try {
            assertNotNull( userService.updateUser("65456e01-1a28-42bf-b217-0bd654", user));
            fail("Expected DataIntegrityViolationException to be thrown");
        } catch (DataIntegrityViolationException e) {
            assertEquals(ErrorMessages.USER_NOT_FOUND, e.getMessage());
        }



    }

}
