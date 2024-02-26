package com.SportRentalInventorySystem.BackEnd.Payload.Request;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javax.validation.constraints.NotBlank;

/**
 *
 * @author minhnguyentranhoang
 */
public class StatusRequest {
   
    private String status;
    
    private String id;
    public String getStatus() {
        return status;
    }

    public void setStatus(String email) {
        this.status = email;
    }
    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }
    
}
