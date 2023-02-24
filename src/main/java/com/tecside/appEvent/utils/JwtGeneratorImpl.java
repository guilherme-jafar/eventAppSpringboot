package com.tecside.appEvent.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.RSAKeyProvider;
import com.tecside.appEvent.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtGeneratorImpl implements JwtGeneratorInterface {

    @Value("${app.jwt.secret}")
    private String secret;
    @Value("${app.jwttoken.message}")
    private String message;

    @Override
    public Map<String, String> generateToken(User user) throws JWTCreationException {

        Algorithm algorithm = Algorithm.HMAC256(this.secret);
        String token = JWT.create()
                .withSubject(user.getEmail())
                .withIssuedAt(new Date())
                .withClaim(user.getEmail(), new Date())
                .withIssuer("auth0")
                .sign(algorithm);
        //TODO: meter o user ID  nmo withClaim, e mudar o withIssuer para um código mandado só pela minha aplicação frontend
        //https://auth0.com/docs/secure/tokens/json-web-tokens/json-web-token-claims


        Map<String, String> jwtTokenGen = new HashMap<>();
        jwtTokenGen.put("token", token);
        jwtTokenGen.put("message", message);
        return jwtTokenGen;


    }

}
