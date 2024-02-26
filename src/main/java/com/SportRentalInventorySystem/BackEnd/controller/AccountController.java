package com.SportRentalInventorySystem.BackEnd.controller;

/**
*
* @author  Meron seyoum
* @version 1.0
* 
* 
* This is the controller for the account functionality in the Sport Rental Inventory System. 
* The controller defines various methods for managing user accounts, such as creating new accounts, logging in, and resetting passwords. 
* It also includes methods for handling authentication and authorization using JWT tokens and email for resetting password.
*/

import com.SportRentalInventorySystem.BackEnd.ExceptionHandler.ResourceNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.mail.javamail.JavaMailSender;
import com.SportRentalInventorySystem.BackEnd.Payload.Request.ForgetPasswordRequest;
import com.SportRentalInventorySystem.BackEnd.Payload.Request.LoginRequest;
import com.SportRentalInventorySystem.BackEnd.Payload.Request.NewPasswordRequest;
import com.SportRentalInventorySystem.BackEnd.Payload.Request.SignupRequest;
import com.SportRentalInventorySystem.BackEnd.Payload.Response.JwtResponse;
import com.SportRentalInventorySystem.BackEnd.Payload.Response.MessageResponse;
import com.SportRentalInventorySystem.BackEnd.Security.services.UserDetailsImpl;
import com.SportRentalInventorySystem.BackEnd.model.ERole;
import com.SportRentalInventorySystem.BackEnd.model.Role;
import com.SportRentalInventorySystem.BackEnd.model.User;
import com.SportRentalInventorySystem.BackEnd.model.UserAddress;
import com.SportRentalInventorySystem.BackEnd.repository.UserRepository;
import com.SportRentalInventorySystem.BackEnd.repository.UserAddressRepository;

import net.bytebuddy.utility.RandomString;

import com.SportRentalInventorySystem.BackEnd.repository.RoleRepository;
import com.SportRentalInventorySystem.BackEnd.Security.jwt.JwtUtils;
import com.SportRentalInventorySystem.BackEnd.services.UserService;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;

//if user does't have account and has a unique Username and email the method will register the user 
// depending on the role the default ROLE is USER but if the Account is created by Manager it may have an ROLE_ADMIN OR ROLE_MODERATOR roles

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private JavaMailSender mailSender;


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            if (userRepository.existsByUsername(signUpRequest.getUsername())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Username is already taken!"));
            }

            if (userRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already in use!"));
            }

            // Create new user's account
            String status = "Active";
            LocalDateTime create_at = LocalDateTime.now();
            User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getEmail(),
                    signUpRequest.getPhoneNumber(), signUpRequest.getUsername(),
                    encoder.encode(signUpRequest.getPassword()), status, create_at);

            Set<String> strRoles = signUpRequest.getRole();
            Set<Role> roles = new HashSet<>();

            if (strRoles == null) {
                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: user Role is not found."));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "admin":
                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Admin Role is not found."));
                            roles.add(adminRole);
                            break;
                        case "mod":
                            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                    .orElseThrow(() -> new RuntimeException("Error: Moderator Role is not found."));
                            roles.add(modRole);
                            break;
                        default:
                            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: User default Role is not found."));
                            roles.add(userRole);
                    }
                });
            }

            user.setRoles(roles);
            userRepository.save(user);

            UserAddress userAddress = new UserAddress();
            userAddress.setUser(user);
            userAddressRepository.save(userAddress);

            return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: " + e.getMessage()));
        }
    }

    
    
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgetPasswordRequest forgetPasswordRequest, Model model) {
        User emailfound = null;
//        check if email exist in database
        
            if (!userRepository.existsByEmail(forgetPasswordRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email Doesn't exist!"));
            }
            
                // if email exist generate token and retrieve the email i n db
                
                 emailfound = userRepository.findByEmail(forgetPasswordRequest.getEmail());
                 
                String token = RandomString.make(30);
                String email = forgetPasswordRequest.getEmail();
                UserService userService = new UserService(userRepository);
            
                try {
        userService.updateResetPasswordToken(token, email);
        String resetPasswordLink = "https://sports-rent.vercel.app/ForgotPassword/" + token;
        
        sendEmail(email, resetPasswordLink);
        //model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
         
    } catch (ResourceNotFoundException | UnsupportedEncodingException | MessagingException ex) {
        return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(ex.getMessage()));
    }
           
        return ResponseEntity.ok(new MessageResponse(emailfound.getEmail()));
    }
    
    @PostMapping("/forgotPassword/token")
    public ResponseEntity<?> forgotPasswordToken(@Valid @RequestBody NewPasswordRequest newPasswordRequest, Model model) {
        User accountfound = null;
        UserService userService = new UserService(userRepository);
        //accountfound = userService.getByResetPasswordToken(newPasswordRequest.getToken());
        accountfound = userService.getByResetPasswordToken(newPasswordRequest.getToken());
        if(accountfound == null){
            return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: The Token does not exist"));
        }
        
        userService.updatePassword(accountfound, newPasswordRequest.getPassword());
        return ResponseEntity.ok(new MessageResponse(accountfound.getEmail()));
    }

    /**
     * send email with a link to a user to change password
     * @param recipientEmail
     * @param link
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    private void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();              
    MimeMessageHelper helper = new MimeMessageHelper(message);
     
    helper.setFrom("sportsrentReset@gmail.com", "Sports Rent Support");
    
    helper.setTo(recipientEmail);
    
    String subject = "Here's the link to reset your password";
     
    String content = "<p>Hello,</p>"
            + "<p>You are receiving this email because we received a password reset request for your account.</p>"
            + "<p>Click the link below to change your password:</p>"
            + "<p><a href=\"" + link + "\">Change my password</a></p>"
            + "<br>"
            + "<p>Ignore this email if you do remember your password, "
            + "or you have not made the request.</p>";
     
    helper.setSubject(subject);
     
    helper.setText(content, true);
     
    mailSender.send(message);
    }

}