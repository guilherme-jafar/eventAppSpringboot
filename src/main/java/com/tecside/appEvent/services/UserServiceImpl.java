package com.tecside.appEvent.services;

import com.tecside.appEvent.models.User;
import com.tecside.appEvent.repositories.UserRepository;
import jdk.jfr.StackTrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;


@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) throws UserPrincipalNotFoundException{


        User user = userRepository.findByEmailAndPassword(email, password);
        if(user == null){
            throw new UserPrincipalNotFoundException("Invalid id and password");
        }
        return user;
    }


}
