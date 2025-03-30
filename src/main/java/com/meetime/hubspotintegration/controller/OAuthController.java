package com.meetime.hubspotintegration.controller;

import com.meetime.hubspotintegration.dto.TokenResponse;
import com.meetime.hubspotintegration.service.OAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthService oAuthService;

    public OAuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @GetMapping("/authorize-url")
    public ResponseEntity<String> getAuthorizationUrl() {
        String url = oAuthService.generateAuthorizationUrl();
        return ResponseEntity.ok(url);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam String code) {
        TokenResponse tokenResponse = oAuthService.exchangeCodeForAccessToken(code);

        System.out.println("Access Token: " + tokenResponse.getAccessToken());

        return ResponseEntity.ok("Token recebido com sucesso! Veja o terminal para o access_token.");
    }
}