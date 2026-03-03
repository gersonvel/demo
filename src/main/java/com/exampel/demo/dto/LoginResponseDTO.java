package com.exampel.demo.dto;

import com.exampel.demo.model.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String refreshToken;
    private Usuario user;
}