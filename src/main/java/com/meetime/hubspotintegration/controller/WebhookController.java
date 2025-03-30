package com.meetime.hubspotintegration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @PostMapping("/contact")
    public ResponseEntity<String> receiveWebhook(@RequestBody String payload) {
        System.out.println("Payload recebido:\n" + payload);
        return ResponseEntity.ok("Recebido com sucesso");
    }
}