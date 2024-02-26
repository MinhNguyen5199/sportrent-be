package com.SportRentalInventorySystem.BackEnd.controller;
/**
 * The UserController class is a RESTful API controller responsible for handling user-related operations.
 * It provides endpoints to retrieve user profiles, update user details, manage user addresses, and change user statuses.
 * Access to these endpoints is restricted based on user roles using Spring Security's @PreAuthorize annotation.
 */

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SportRentalInventorySystem.BackEnd.ExceptionHandler.ResourceNotFoundException;
import com.SportRentalInventorySystem.BackEnd.Payload.Request.NewPasswordRequest;
import com.SportRentalInventorySystem.BackEnd.Payload.Request.StatusRequest;
import com.SportRentalInventorySystem.BackEnd.Payload.Response.MessageResponse;
import com.SportRentalInventorySystem.BackEnd.model.Product;
import com.SportRentalInventorySystem.BackEnd.model.User;
import com.SportRentalInventorySystem.BackEnd.model.UserAddress;
import com.SportRentalInventorySystem.BackEnd.repository.UserAddressRepository;
import com.SportRentalInventorySystem.BackEnd.repository.UserRepository;
import com.SportRentalInventorySystem.BackEnd.services.UserService;

@CrossOrigin(origins = "*" ) 
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserAddressRepository userAddressRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;
    
    
    
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    
    /**
     * Retrieves a list of all users.
     * 
     * @return List of User objects
     */
    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public  List<User> getUser(){
        return userRepository.findAll();
    }
    
    
/**
    * Retrieves the profile data for a user by ID.
    * 
    * @param id User ID
    * @return ResponseEntity containing the User object
    */
    @GetMapping("/getUserProfileById/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER') OR hasRole('ROLE_MODERATOR')")
    public ResponseEntity<User> getProfile(@PathVariable long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id:" + id));
        return ResponseEntity.ok(user);
    }
    

    /**
     * Retrieves the address for a user by ID.
     * 
     * @param id User ID
     * @return ResponseEntity containing the UserAddress object
     */
    @GetMapping("/getUserAddress/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER') OR hasRole('ROLE_MODERATOR')")
    public ResponseEntity<UserAddress> getUserAddress(@PathVariable long id) {
        Optional<UserAddress> userAddressOptional = userAddressRepository.findByForeignId(id);
        if (userAddressOptional.isPresent()) {
            UserAddress userAddress = userAddressOptional.get();
            return ResponseEntity.ok(userAddress);
        } else {
            throw new ResourceNotFoundException("User Address does not exist with user_id: " + id);
        }
    }
    
  /**
   * Changes the status of a user by ID.
   * 
   * @param id     User ID
   * @param status New status
   * @return ResponseEntity indicating success or failure
   */
  @PutMapping("/changeStatusById/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER') OR hasRole('ROLE_MODERATOR')")
  public ResponseEntity<?> changeStatusById(@PathVariable("id") long id, @RequestParam("status") String status) {
      User user = userRepository.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("User does not exist with id: " + id));

      user.setStatus(status);
      userRepository.save(user);

      return ResponseEntity.ok().build();
  }
  
  /**
   * Updates the details of a user by ID.
   * 
   * @param id   User ID
   * @param user Updated User object
   * @return ResponseEntity containing the updated User object
   */
  @PutMapping("/updateUser/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER') OR hasRole('ROLE_MODERATOR')")
  public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User user) {
      User updateUser = userRepository.findById(id)
              .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

      updateUser.setFirstName(user.getFirstName());
      updateUser.setLastName(user.getLastName());
      updateUser.setPhoneNumber(user.getPhoneNumber());

      userRepository.save(updateUser);

      return ResponseEntity.ok(updateUser);
  }

  /**
   * Updates the address of a user by ID.
   * 
   * @param id          User ID
   * @param userAddress Updated UserAddress object
   * @return ResponseEntity containing the updated UserAddress object
   */
  @PutMapping("/updateAddress/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER') OR hasRole('ROLE_MODERATOR')")
  public ResponseEntity<UserAddress> updateUserAddress(@PathVariable long id, @RequestBody UserAddress userAddress) {
      UserAddress updateUserAddress = userAddressRepository.findByForeignId(id)
              .orElseThrow(() -> new ResourceNotFoundException("User address not found with userId: " + id));

      updateUserAddress.setAddress(userAddress.getAddress());
      updateUserAddress.setCity(userAddress.getCity());
      updateUserAddress.setProvince(userAddress.getProvince());
      updateUserAddress.setCountry(userAddress.getCountry());
      updateUserAddress.setZipCode(userAddress.getZipCode());

      userAddressRepository.save(updateUserAddress);

      return ResponseEntity.ok(updateUserAddress);
  }


  // ...
  @PutMapping("/updatePassword/{id}")
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> updatePassword(@PathVariable("id") long id, @RequestBody NewPasswordRequest newPasswordRequest) {
      User user = userRepository.findById(id)
          .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

      userService.updateNewPassword(user, newPasswordRequest.getPassword());
      
      return ResponseEntity.ok("Password updated successfully");
  }


} 