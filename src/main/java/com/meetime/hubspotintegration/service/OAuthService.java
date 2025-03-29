package com.meetime.hubspotintegration.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
}
