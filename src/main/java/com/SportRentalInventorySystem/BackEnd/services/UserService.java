package com.SportRentalInventorySystem.BackEnd.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.SportRentalInventorySystem.BackEnd.repository.UserRepository;
import com.SportRentalInventorySystem.BackEnd.ExceptionHandler.ResourceNotFoundException;
import com.SportRentalInventorySystem.BackEnd.model.User;

@Service
public class UserService {
    
    @Autowired
    public UserRepository userRepository;
    
    public UserService(UserRepository userRepository){
        this.userRepository =userRepository;
    }
    
    @SuppressWarnings("unused")
    public void updateResetPasswordToken(String token, String email) throws ResourceNotFoundException {
        
        User user = userRepository.findByEmail(email);
       if (user != null) {
            user.setResetPasswordToken(token);
            userRepository.save(user);
        } else {
            throw new ResourceNotFoundException("Could not find any customer with the email " + user.getEmail());
        }
    }
     
    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }
     
    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        
        user.setResetPasswordToken(null);
        userRepository.save(user); 
      
    }
    
    public void updateNewPassword(User user, String newPassword) {
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//    	String encodedPassword = newPasswordEncoder.encode(newPassword);
    	 user.setPassword(passwordEncoder.encode(newPassword));
    	    userRepository.save(user);
      }
}
