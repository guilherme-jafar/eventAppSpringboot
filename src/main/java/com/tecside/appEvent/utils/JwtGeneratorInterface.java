package com.tecside.appEvent.utils;

import com.tecside.appEvent.models.User;
import java.util.Map;
public interface JwtGeneratorInterface {

    Map<String, String> generateToken(User user);

}
