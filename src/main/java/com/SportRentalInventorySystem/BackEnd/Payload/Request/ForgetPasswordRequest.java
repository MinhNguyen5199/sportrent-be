package com.SportRentalInventorySystem.BackEnd.Payload.Request;

import javax.validation.constraints.NotBlank;

public class ForgetPasswordRequest {
    @NotBlank
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
