package com.meetime.hubspotintegration.service;

import com.meetime.hubspotintegration.dto.ContactRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.meetime.hubspotintegration.dto.TokenResponse;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class OAuthService {

    private final RestTemplate restTemplate;

    public OAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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

    public void createContact(ContactRequest contact, String accessToken) {
        String url = "https://api.hubapi.com/crm/v3/objects/contacts";

        Map<String, Object> properties = new HashMap<>();
        properties.put("email", contact.getEmail());
        properties.put("firstname", contact.getFirstName());
        properties.put("lastname", contact.getLastName());

        Map<String, Object> requestBody = Map.of("properties", properties);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        System.out.println("Contato criado com status: " + response.getStatusCode());
        System.out.println("Resposta: " + response.getBody());
    }

    public String getContacts(String accessToken) {
        String url = "https://api.hubapi.com/crm/v3/objects/contacts";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }
}