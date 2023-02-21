package com.tecside.appEvent.services;


import com.tecside.appEvent.models.User;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;

@Service
public interface UserService {

    public void saveUser(User user);
    public User getUserByEmailAndPassword(String name, String password) throws UserPrincipalNotFoundException;


}
