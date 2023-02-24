package com.tecside.appEvent.services;


import com.tecside.appEvent.models.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@Service
public interface UserService {

    public void saveUser(User user)throws DataIntegrityViolationException;
    public User getUserByEmail(String name, String password) throws UserPrincipalNotFoundException;

    public Optional<User> getUserById(Long id);

    public User updateUser(Long userId, User user)throws DataIntegrityViolationException;


}
