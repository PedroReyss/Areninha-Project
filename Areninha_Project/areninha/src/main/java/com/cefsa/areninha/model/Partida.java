package com.cefsa.areninha.model;

import java.time.LocalDateTime;

public class Partida {
    private int id;
    private int usuarioId;
    private String tipoJogo;
    private int pontuacao;
    private LocalDateTime dataPartida;
    private String resultado;
    
    public Partida() {}
    
    public Partida(int usuarioId, String tipoJogo, int pontuacao, String resultado) {
        this.usuarioId = usuarioId;
        this.tipoJogo = tipoJogo;
        this.pontuacao = pontuacao;
        this.dataPartida = LocalDateTime.now();
        this.resultado = resultado;
    }
    
    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public String getTipoJogo() { return tipoJogo; }
    public void setTipoJogo(String tipoJogo) { this.tipoJogo = tipoJogo; }
    public int getPontuacao() { return pontuacao; }
    public void setPontuacao(int pontuacao) { this.pontuacao = pontuacao; }
    public LocalDateTime getDataPartida() { return dataPartida; }
    public void setDataPartida(LocalDateTime dataPartida) { this.dataPartida = dataPartida; }
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
}