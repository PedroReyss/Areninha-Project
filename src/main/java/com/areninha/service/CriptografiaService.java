package com.areninha.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CriptografiaService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String criptografarSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    public boolean verificarSenha(String senha, String senhaCriptografada) {
        return passwordEncoder.matches(senha, senhaCriptografada);
    }
}