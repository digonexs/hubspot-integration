package com.meetime.hubspotintegration.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @PostMapping("/contact")
    public ResponseEntity<String> receiveContactWebhook(@RequestBody String payload, @RequestHeader("X-HubSpot-Signature") String signature) {
        System.out.println("Webhook recebido!");
        System.out.println("Assinatura: " + signature);
        System.out.println("Payload: " + payload);

        return new ResponseEntity<>("Webhook recebido com sucesso", HttpStatus.OK);
    }
}