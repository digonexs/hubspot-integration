package com.meetime.hubspotintegration.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@Tag(name = "Webhook", description = "Recebe eventos do HubSpot via webhook (criação de contatos)")
public class WebhookController {

    @PostMapping("/contact")
    @Operation(
            summary = "Receber eventos de criação de contato",
            description = "Endpoint que recebe os eventos de criação de contato enviados pelo HubSpot. Requer que o webhook esteja configurado no app do desenvolvedor."
    )
    public ResponseEntity<String> receiveWebhook(
            @RequestBody @Parameter(description = "Payload JSON enviado automaticamente pelo HubSpot") String payload
    ) {
        try {
            System.out.println("Payload recebido:\n" + payload);
            return ResponseEntity.ok("Recebido com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao processar webhook: " + e.getMessage());
        }
    }
}