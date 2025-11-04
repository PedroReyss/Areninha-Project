package com.areninha.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "partida")
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;

    @Column(name = "pontuacao")
    private Integer pontuacao = 0;

    @Column(name = "nivel_alcancado")
    private Integer nivelAlcancado = 1;

    @Column(name = "tempo_jogo_segundos")
    private Integer tempoJogoSegundos;

    @Column(name = "data_partida")
    private LocalDateTime dataPartida;

    @Column(name = "concluida")
    private Boolean concluida = false;

    // Construtores
    public Partida() {
        this.dataPartida = LocalDateTime.now();
    }

    public Partida(Usuario usuario, Jogo jogo) {
        this();
        this.usuario = usuario;
        this.jogo = jogo;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Jogo getJogo() { return jogo; }
    public void setJogo(Jogo jogo) { this.jogo = jogo; }

    public Integer getPontuacao() { return pontuacao; }
    public void setPontuacao(Integer pontuacao) { this.pontuacao = pontuacao; }

    public Integer getNivelAlcancado() { return nivelAlcancado; }
    public void setNivelAlcancado(Integer nivelAlcancado) { this.nivelAlcancado = nivelAlcancado; }

    public Integer getTempoJogoSegundos() { return tempoJogoSegundos; }
    public void setTempoJogoSegundos(Integer tempoJogoSegundos) { this.tempoJogoSegundos = tempoJogoSegundos; }

    public LocalDateTime getDataPartida() { return dataPartida; }
    public void setDataPartida(LocalDateTime dataPartida) { this.dataPartida = dataPartida; }

    public Boolean getConcluida() { return concluida; }
    public void setConcluida(Boolean concluida) { this.concluida = concluida; }
}