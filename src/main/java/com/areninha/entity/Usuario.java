package com.areninha.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nick", unique = true, nullable = false, length = 15)
    private String nick;

    @Column(name = "email", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(name = "data_ultima_aparicao")
    private LocalDateTime dataUltimaAparicao;

    @Column(name = "nivel")
    private Integer nivel = 1;

    @Column(name = "experiencia")
    private Integer experiencia = 0;

    @Column(name = "ativo")
    private Boolean ativo = true;

    // Construtores
    public Usuario() {}

    public Usuario(String nick, String email, String senha) {
        this.nick = nick;
        this.email = email;
        this.senha = senha;
        this.dataCadastro = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.dataUltimaAparicao = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNick() { return nick; }
    public void setNick(String nick) { this.nick = nick; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }

    public LocalDateTime getDataUltimaAparicao() { return dataUltimaAparicao; }
    public void setDataUltimaAparicao(LocalDateTime dataUltimaAparicao) { this.dataUltimaAparicao = dataUltimaAparicao; }

    public Integer getNivel() { return nivel; }
    public void setNivel(Integer nivel) { this.nivel = nivel; }

    public Integer getExperiencia() { return experiencia; }
    public void setExperiencia(Integer experiencia) { this.experiencia = experiencia; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}