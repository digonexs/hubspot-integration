package com.meetime.hubspotintegration.controller;

import com.meetime.hubspotintegration.dto.TokenResponse;
import com.meetime.hubspotintegration.dto.ContactRequest;
import com.meetime.hubspotintegration.service.OAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthService oauthService;

    public OAuthController(OAuthService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/authorize-url")
    public ResponseEntity<String> getAuthorizationUrl() {
        String url = oauthService.generateAuthorizationUrl();
        return ResponseEntity.ok(url);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam String code) {
        TokenResponse tokenResponse = oauthService.exchangeCodeForAccessToken(code);
        System.out.println("Access Token: " + tokenResponse.getAccessToken());
        return ResponseEntity.ok("Token recebido com sucesso! Veja o terminal para o access_token.");
    }

    @PostMapping("/contacts")
    public ResponseEntity<String> createContact(
            @RequestBody ContactRequest request,
            @RequestHeader("Authorization") String bearerToken
    ) {
        String token = bearerToken.replace("Bearer ", "");
        oauthService.createContact(request, token);
        return ResponseEntity.ok("Contato enviado para criação");
    }

    @GetMapping("/contacts")
    public ResponseEntity<String> getContacts(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        String contactsJson = oauthService.getContacts(token);
        return ResponseEntity.ok(contactsJson);
    }
}