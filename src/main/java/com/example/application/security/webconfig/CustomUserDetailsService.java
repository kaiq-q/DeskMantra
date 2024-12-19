package com.example.application.security.webconfig;

import com.example.application.data.Repositories.UserRepository;
import com.example.application.data.Role;
import com.example.application.data.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByUsername(username); // Fetch user from database
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return org.springframework.security.core.userdetails.User.builder()

                .username(user.getLogin())
                .password(user.getPassword())
                // Assuming you store hashed passwords
                .roles(user.getRoles().stream().map(Role::getName).toArray(String[]::new)) // Assuming you have a method to get roles
                .build();
    }


}
