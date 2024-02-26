/**
* @author  Meron Seyoum
* @version 1.0
* @since   2022-09-24
*
*This code is a Java class called User that represents a user in a system. 
*The class has fields for the user's id, username, email, password, first name, 
*last name, phone number, and various other properties. 
*The class also has methods for setting and getting the values of these fields. 
*The class is marked with @Entity and @Table annotations, which means it is a JPA entity and is mapped to a database table.
* It also has constraints specified using the @NotBlank, @Size, and @Email annotations to validate the values of certain fields.
*/
package com.SportRentalInventorySystem.BackEnd.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;
    @NotBlank
    @Size(max = 20)

    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Size(max = 20)
    @Column(name = "last_name")
    private String lastName;

    @Size(max = 15)
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "status")
    private String status;

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

    @Column(name = "create_at", columnDefinition = "DATE")
    private LocalDateTime create_at;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

//    @OneToOne(fetch = FetchType.LAZY,
//            cascade =  CascadeType.ALL,
//            mappedBy = "user")
//    private UserAddress userAddress;

    public User() {
    	 this.create_at = LocalDateTime.now();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public User(String firstName, String lastName,  String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;       
    }
    
    public User(String firstName, String lastName, String email, String phoneNumber, String username, String password,
            String status, LocalDateTime create_at) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.password = password;
        this.status = status;
        this.create_at = create_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }

    public LocalDateTime getCreate_at() {
        return create_at;
    }

    public void setCreate_at(LocalDateTime create_at) {
        this.create_at = create_at;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

//    public UserAddress getUserAddress() {
//        return userAddress;
//    }
//
//    public void setUserAddress(UserAddress userAddress) {
//        this.userAddress = userAddress;
//    }

}