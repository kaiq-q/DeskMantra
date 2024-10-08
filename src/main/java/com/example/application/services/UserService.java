package com.example.application.services;

import com.example.application.data.Repositories.RoleRepository;
import com.example.application.data.Repositories.UserRepository;
import com.example.application.data.Role;
import com.example.application.data.Users;
import com.example.application.security.EmailServiceImpl;
import com.example.application.security.Emails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository userRoleRepository;
    //private final BCryptPasswordEncoder passwordEncoder;
    private final EmailServiceImpl emailService;

    public UserService(UserRepository userRepository, RoleRepository userRoleRepository, EmailServiceImpl emailService){
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        //this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public Users findById(Integer id){
        return userRepository.findById(id)
                .orElse(null);
    }

    public Users findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public List<Users> findAllUsers(String filter){
       if (filter == null || filter.isEmpty()){
            List<Users> userList = new ArrayList<>();
            userRepository.findAll().forEach(userList::add);
            return userList;
       }else{
           return userRepository.search(filter);
       }

    }

    public long countUser(){
        return userRepository.count();
    }

    public void deleteUser(Users user){
        userRepository.delete(user);
    }

    public void saveUser(Users user){
        if (user == null){
            System.err.println("User is null. Are you sure you have connected your form to the application");
            return;
        }
       //user.setPassword(encryptPassword(generateRandomPassword()));
        String generatedPassword = generateRandomPassword();
        emailService.sendEmail(user.getEmail(), "Welcome to Desk Mantra", "Your password is: " + generatedPassword);
        userRepository.save(user);
    }

    public List<Role> findAllRoles(){
        List<Role> roles = new ArrayList<>();
        userRoleRepository.findAll().forEach(roles::add);
        return roles;
    }




    // Method to generate a random password
    private String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@";
        int length = 8;
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }
//
//    // Method to encrypt a password using BCrypt
//    private String encryptPassword(String password) {
//        return passwordEncoder.encode(password);
//    }

}


