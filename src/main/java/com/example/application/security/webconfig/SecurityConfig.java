package com.example.application.security.webconfig;

import com.example.application.views.Security.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@Configuration
public class SecurityConfig extends VaadinWebSecurity {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Disable CSRF for Vaadin endpoints
        http.csrf(csrf -> csrf
                .ignoringRequestMatchers("/VAADIN/**", "/api/**")
        );

        // Allow access to static resources and login page
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                        antMatchers("/VAADIN/**",
                                "/frontend/**",
                                "/images/**",
                                "/frontend-es5/**",
                                "/frontend-es6/**",
                                "/api/**",
                                "/login")
                ).permitAll()

        );

        // Set login view
        setLoginView(http, LoginView.class);

        // Configure Vaadin security
        super.configure(http);
    }

    @Bean
    UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

