package com.SportRentalInventorySystem.BackEnd.controller;

/**
 *  @author  Meron Seyoum
 * @version 1.0
 *
 *The code is a controller class for managing user accounts in a Spring Boot application. 
 *The class defines various methods for creating, updating, retrieving, and deleting user accounts. 
 *It also defines methods for searching user accounts based on keywords and for creating, updating, and retrieving user addresses. 
 *The controller uses the @RestController and @RequestMapping annotations to define its REST API end points. 
 *It also uses the @PreAuthorize annotation to specify which roles are allowed to access each end point.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.SportRentalInventorySystem.BackEnd.ExceptionHandler.ResourceNotFoundException;

import com.SportRentalInventorySystem.BackEnd.model.User;
import com.SportRentalInventorySystem.BackEnd.model.UserAddress;
import com.SportRentalInventorySystem.BackEnd.repository.UserAddressRepository;
import com.SportRentalInventorySystem.BackEnd.repository.UserRepository;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/Customer")
public class CustomerController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserAddressRepository userAddressRepository;
    

    /**
     * User account information managed by admin. manager CRUD operation is done
     * here
     * 
     * @param user
     * @return
     */

    @PostMapping("/createUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }

    /**
     * build update user REST API
     * 
     * @param id
     * @param userDetails
     * @return
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER') OR hasRole('ROLE_MODERATOR')")
    public ResponseEntity<User> updateUser(@PathVariable long id, @RequestBody User userDetails ) {
        User updateUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + id));
        updateUser.setFirstName(userDetails.getFirstName());
        updateUser.setLastName(userDetails.getLastName());
        updateUser.setPhoneNumber(userDetails.getPhoneNumber());
               
        userRepository.save(updateUser);

        return ResponseEntity.ok(updateUser);
    }

    /**
     * Retrieve User information and send to client
     * 
     * @return
     */
    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    /**
     * build get user by id REST API
     * 
     * @param id
     * @return
     */
    @GetMapping("/userById/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER') OR hasRole('ROLE_MODERATOR')")
     public ResponseEntity<User> getUser(@PathVariable long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id:" + id));
        return ResponseEntity.ok(user);
    }

    /**
     * delete user REST API
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/deleteUser/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not exist with id: " + id));

        userRepository.delete(user);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//  User search based on the given keyword
  @GetMapping("/searchUser/{keyWords}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public ResponseEntity<?> SearchUser(@PathVariable String keyWords) {
      return new ResponseEntity<>(userRepository.searchByUserName(keyWords), HttpStatus.OK);
  }
}