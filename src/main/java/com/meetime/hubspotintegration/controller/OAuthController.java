package com.meetime.hubspotintegration.controller;

import com.meetime.hubspotintegration.dto.TokenResponse;
import com.meetime.hubspotintegration.dto.ContactRequest;
import com.meetime.hubspotintegration.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/oauth")
@Tag(name = "OAuth / Contatos", description = "Gerencia autenticação com HubSpot e manipulação de contatos")
public class OAuthController {

    private final OAuthService oauthService;

    public OAuthController(OAuthService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/authorize-url")
    @Operation(summary = "Gerar URL de autorização", description = "Gera a URL para o usuário autenticar com o HubSpot e permitir acesso à aplicação")
    public ResponseEntity<String> getAuthorizationUrl() {
        try {
            String url = oauthService.generateAuthorizationUrl();
            return ResponseEntity.ok(url);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar URL de autorização: " + ex.getMessage());
        }
    }

    @GetMapping("/callback")
    @Operation(summary = "Receber código de autorização", description = "Endpoint chamado pelo HubSpot após o usuário autorizar o app. Recebe o 'code' e troca por access_token.")
    public ResponseEntity<String> callback(
            @RequestParam @Parameter(description = "Código de autorização enviado pelo HubSpot") String code
    ) {
        try {
            TokenResponse tokenResponse = oauthService.exchangeCodeForAccessToken(code);
            System.out.println("Access Token: " + tokenResponse.getAccessToken());
            return ResponseEntity.ok("Token recebido com sucesso! Veja o terminal para o access_token.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro ao processar callback: " + ex.getMessage());
        }
    }

    @PostMapping("/contacts")
    @Operation(summary = "Criar novo contato", description = "Cria um novo contato no HubSpot usando o token OAuth do usuário autenticado")
    public ResponseEntity<String> createContact(
            @RequestBody @Parameter(description = "Objeto com nome, sobrenome e email do contato") ContactRequest request,
            @RequestHeader("Authorization") @Parameter(description = "Token OAuth do tipo Bearer") String bearerToken
    ) {
        String token = bearerToken.replace("Bearer ", "");

        if (request.getFirstName() == null || request.getFirstName().isBlank() ||
                request.getLastName() == null || request.getLastName().isBlank() ||
                request.getEmail() == null || request.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Erro: Todos os campos (firstName, lastName, email) são obrigatórios.");
        }

        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ResponseEntity.badRequest().body("Erro: Endereço de e-mail inválido.");
        }

        try {
            oauthService.createContact(request, token);
            return ResponseEntity.ok("Contato enviado para criação");
        } catch (HttpClientErrorException.BadRequest ex) {
            return ResponseEntity.badRequest().body("Erro: Verifique os campos enviados. O e-mail pode estar inválido.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar contato: " + ex.getMessage());
        }
    }

    @GetMapping("/contacts")
    @Operation(summary = "Listar contatos", description = "Retorna a lista de contatos do usuário autenticado no HubSpot")
    public ResponseEntity<String> getContacts(
            @RequestHeader("Authorization") @Parameter(description = "Token OAuth do tipo Bearer") String bearerToken
    ) {
        try {
            String token = bearerToken.replace("Bearer ", "");
            String contactsJson = oauthService.getContacts(token);
            return ResponseEntity.ok(contactsJson);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao listar contatos: " + ex.getMessage());
        }
    }
}