package com.meetime.hubspotintegration.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactRequest {

    @NotBlank(message = "O campo 'firstName' é obrigatório")
    private String firstName;

    @NotBlank(message = "O campo 'lastName' é obrigatório")
    private String lastName;

    @NotBlank(message = "O campo 'email' é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;
}