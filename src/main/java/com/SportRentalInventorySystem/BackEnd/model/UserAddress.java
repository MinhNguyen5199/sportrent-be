package com.SportRentalInventorySystem.BackEnd.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user_address")
public class UserAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50)
    private String address;

//    @Size(max = 50)
//    private String address2;

    @Size(max = 20)
    private String city;

    @Size(max = 50)
    private String province;

    @Size(max = 50)
    private String country;


    @Column(name = "zip_code")
    @Size(max = 32)
    private String zipCode;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserAddress() {

    }
    public UserAddress(Long id) {

        this.id = id;
    }

    public UserAddress(Long id, String address, String city, String province, String country, String zipCode) {
        super();
        this.id = id;
        this.address = address; 
        this.city = city;
        this.province = province;
        this.country = country;
        this.zipCode = zipCode;
       
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

//    public String getAddress2() {
//        return address2;
//    }
//
//    public void setAddress2(String address2) {
//        this.address2 = address2;
//    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
  

  

}
