package com.tecside.appEvent.configs;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tecside.appEvent.utils.JwtFilter;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Value("${app.jwt.secret}")
    private String secret;

    @Bean
    public FilterRegistrationBean jwtFilter(){



        FilterRegistrationBean filter = new FilterRegistrationBean();
        filter.setFilter(new JwtFilter(this.secret));

        // provide endpoints which needs to be restricted.
        // All Endpoints would be restricted if unspecified
        filter.addUrlPatterns("/api/v1/categories/*");


        return filter;
    }


}
