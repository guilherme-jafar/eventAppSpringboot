package com.tecside.appEvent.errors;

import org.springframework.context.annotation.Bean;

public class ErrorMessages {

    public static final String MISSING_EMAIL = "Email is required";

    public static final String MISSING_NAME = "Name is required";

    public static final String MISSING_IMAGE= "Image is required";

    public static final String WRONG_IMAGE_EXTENSION= "Image extension not allowed";
    public static final String WRONG_EMAIL_FORMAT = "Wrong email format";

    public static final String EMAIL_ALREADY_EXISTS = "User email already exists";

    public static final String WRONG_NUMBER_FORMAT = "Wrong number format";
    public static final String MISSING_PASSWORD = "Password is required";

    public static final String MISSING_PASSWORD_OR_EMAIL = "Email or Password is Empty";

    public static final String USER_NOT_FOUND = "User not found";

    public static final String CATEGORY_NOT_FOUND = "Category not found";

    public static final String INVALID_PASSWORD_EMAIL = "Invalid email or password";

    public static final String GENERAL_ERROR = "An error occurred while processing your request. Please try again later";




    public static String errorJSON(String message){
        return "{\"error\": \""+message+ "\"}";
    }

}
