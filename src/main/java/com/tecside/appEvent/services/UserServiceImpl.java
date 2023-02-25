package com.tecside.appEvent.services;

import com.tecside.appEvent.errors.ErrorMessages;
import com.tecside.appEvent.models.User;
import com.tecside.appEvent.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        if (email == null) {
            throw new DataIntegrityViolationException(ErrorMessages.MISSING_EMAIL);
        }


        if (password == null) {
            throw new DataIntegrityViolationException(ErrorMessages.MISSING_PASSWORD);
        }


        if (!isValidEmailFormat(email)) {
            throw new DataIntegrityViolationException(ErrorMessages.WRONG_EMAIL_FORMAT);
        }


        Date createdAta = new Date();
        user.setCreatedAt(createdAta);
        user.setPassword(passwordEncoder().encode(user.getPassword()));
        user.setTypeOfLogin("NORMAL");


        userRepository.saveAndFlush(user);
    }

    @Override
    public User login(String email, String password) throws BadCredentialsException {


        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new BadCredentialsException(ErrorMessages.INVALID_PASSWORD_EMAIL);
        }

        if (!passwordEncoder().matches(password, user.getPassword()) ) {
            throw new BadCredentialsException(ErrorMessages.INVALID_PASSWORD_EMAIL);
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
            throw new DataIntegrityViolationException(ErrorMessages.USER_NOT_FOUND);

        }

        User user = optional.get();


        String mobileNumber = updatedUser.getMobileNumber();

        if (mobileNumber != null) {
            if (mobileNumber.length() != 9) {
                throw new DataIntegrityViolationException(ErrorMessages.WRONG_NUMBER_FORMAT);
            }

            user.setMobileNumber(mobileNumber);
        }


        String password = updatedUser.getPassword();

        if (password != null) {
            user.setPassword(passwordEncoder().encode(password));

        }

        String countryCode = updatedUser.getCountryCode();

        if (countryCode != null) {
            user.setCountryCode(countryCode);
        }
        user.setUpdatedAt(new Date());
        user.setFullName(updatedUser.getFullName());


        return userRepository.saveAndFlush(user);


    }

    public void deleteUser(Long userId) throws EmptyResultDataAccessException {

        userRepository.deleteById(userId);

    }


    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    public boolean isValidEmailFormat(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        return email.matches(regex);
    }


}
