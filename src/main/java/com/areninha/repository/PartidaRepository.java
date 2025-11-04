package com.areninha.repository;

import com.areninha.entity.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PartidaRepository extends JpaRepository<Partida, Long> {
    List<Partida> findByUsuarioIdOrderByDataPartidaDesc(Long usuarioId);
    List<Partida> findByJogoIdOrderByPontuacaoDesc(Long jogoId);

    @Query("SELECT p FROM Partida p WHERE p.usuario.id = :usuarioId AND p.jogo.id = :jogoId ORDER BY p.pontuacao DESC")
    List<Partida> findTopByUsuarioAndJogo(Long usuarioId, Long jogoId);
}