package com.tecside.appEvent.services;

import com.tecside.appEvent.models.User;
import com.tecside.appEvent.repositories.UserRepository;
import jdk.jfr.StackTrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Date;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public void saveUser(User user) throws DataIntegrityViolationException {


        Date createdAta = new Date();
        user.setCreatedAt(createdAta);
        user.setPassword(passwordEncoder().encode(user.getPassword()));

        userRepository.save(user);
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) throws UserPrincipalNotFoundException{


        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new UserPrincipalNotFoundException("Account don't exist");
        }

        if (!passwordEncoder().matches(password, user.getPassword())) {
            throw new UserPrincipalNotFoundException("Invalid email or password");
        }


        return user;
    }

    @Override
    public Optional<User> getUserById(Long id){

        return userRepository.findById(id);

    }

    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }


}
