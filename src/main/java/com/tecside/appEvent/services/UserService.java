package com.tecside.appEvent.services;


import com.tecside.appEvent.models.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Service
public interface UserService {

    User saveUser(User user)throws DataIntegrityViolationException;
    User login(String name, String password) throws BadCredentialsException;

    Optional<User> getUserById(String  id);

    User updateUser(String userId, User user)throws DataIntegrityViolationException;


    void deleteUser(String userId);

}
