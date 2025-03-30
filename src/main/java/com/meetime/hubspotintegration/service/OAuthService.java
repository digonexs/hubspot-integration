package com.meetime.hubspotintegration.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.meetime.hubspotintegration.dto.TokenResponse;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class OAuthService {

    @Value("${hubspot.oauth.client.id}")
    private String clientId;

    @Value("${hubspot.oauth.client.secret}")
    private String clientSecret;

    @Value("${hubspot.oauth.redirect.uri}")
    private String redirectUri;

    @Value("${hubspot.oauth.scope}")
    private String scope;

    public String generateAuthorizationUrl() {
        System.out.println("DEBUG - clientId: " + clientId);
        System.out.println("DEBUG - redirectUri: " + redirectUri);
        System.out.println("DEBUG - scope: " + scope);

        if (clientId == null || redirectUri == null || scope == null) {
            throw new IllegalStateException("Parâmetros OAuth não configurados corretamente.");
        }

        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        String encodedScope = URLEncoder.encode(scope, StandardCharsets.UTF_8);

        return String.format("https://app.hubspot.com/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s&response_type=code",
                clientId, encodedRedirectUri, encodedScope);
    }

    public TokenResponse exchangeCodeForAccessToken(String code) {
        String tokenUrl = "https://api.hubapi.com/oauth/v1/token";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<TokenResponse> response = restTemplate.postForEntity(tokenUrl, request, TokenResponse.class);

        return response.getBody();
    }
}