package com.SportRentalInventorySystem.BackEnd.controller;


import com.SportRentalInventorySystem.BackEnd.Security.services.StripeClient;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/payment")
public class PaymentController {

    private final StripeClient stripeClient;

    @Autowired
    public PaymentController(StripeClient stripeClient) {
        this.stripeClient = stripeClient;
    }

    @PostMapping("/charge")
    public ResponseEntity<String> chargeCard(@RequestHeader(value = "token") String token, @RequestHeader(value = "amount") Double amount) {
        try {
             stripeClient.chargeNewCard(token, amount);
            // Payment successful
            return ResponseEntity.ok("Payment successful");
        } catch (Exception e) {
            // Payment failed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment failed: " + e.getMessage());
        }
    }
}
