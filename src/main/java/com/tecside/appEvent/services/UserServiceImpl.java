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


        String email = user.getEmail();
        String password = user.getPassword();

        if (email == null || !isValidEmailFormat(email)) {
            throw new DataIntegrityViolationException("Wrong Email Format");
        }

        if (password == null) {
            throw new DataIntegrityViolationException("Password can't be empty");
        }


        Date createdAta = new Date();
        user.setCreatedAt(createdAta);
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        user.setTypeOfLogin("NORMAL");


        userRepository.saveAndFlush(user);
    }

    @Override
    public User getUserByEmail(String email, String password) throws UserPrincipalNotFoundException {


        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserPrincipalNotFoundException("Account don't exist");
        }

        if (!passwordEncoder().matches(password, user.getPassword())) {
            throw new UserPrincipalNotFoundException("Invalid email or password");
        }


        return user;
    }

    @Override
    public Optional<User> getUserById(Long id) {

        return userRepository.findById(id);

    }

    @Override
    public User updateUser(Long userId, User updatedUser) throws DataIntegrityViolationException {

        Optional<User> optional = userRepository.findById(userId);
        if (optional.isEmpty()) {
            throw new DataIntegrityViolationException("User not found");

        }

        User user = optional.get();


        if (updatedUser.getFullName() == null) {
            throw new DataIntegrityViolationException("User name can't be empty");
        }

        String mobileNumber = updatedUser.getMobileNumber();

        if (mobileNumber != null) {
            if (mobileNumber.length() != 9) {
                throw new DataIntegrityViolationException("Wrong number format");
            }

            user.setMobileNumber(mobileNumber);
        }


        String password = updatedUser.getPassword();

        if (password != null) {
            user.setPassword(passwordEncoder().encode(password));

        }

        String countryCode = updatedUser.getCountryCode();

        if (countryCode!= null){
            user.setCountryCode(countryCode);
        }
        user.setUpdatedAt(new Date());
        user.setFullName(updatedUser.getFullName());



        return userRepository.saveAndFlush(user);


    }


    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    public boolean isValidEmailFormat(String email){
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        return email.matches(regex);
    }


}
