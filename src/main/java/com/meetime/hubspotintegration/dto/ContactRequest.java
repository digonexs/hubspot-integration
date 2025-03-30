package com.meetime.hubspotintegration.dto;

import lombok.Data;

@Data
public class ContactRequest {
    private String email;
    private String firstName;
    private String lastName;
}