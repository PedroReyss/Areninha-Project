package com.cefsa.areninha.dao;

import com.cefsa.areninha.model.Partida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PartidaDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void criarTabela() {
        try {
            String sql = """
                IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='partidas' AND xtype='U')
                CREATE TABLE partidas (
                    id INT IDENTITY(1,1) PRIMARY KEY,
                    usuario_id INT,
                    tipo_jogo VARCHAR(50),
                    pontuacao INT,
                    data_partida DATETIME,
                    resultado VARCHAR(255),
                    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
                )
                """;
            jdbcTemplate.execute(sql);
            System.out.println("✅ Tabela 'partidas' criada ou já existente");
        } catch (DataAccessException e) {
            System.err.println("❌ Erro ao criar tabela partidas: " + e.getMessage());
        }
    }

    public void salvar(Partida partida) {
        try {
            String sql = "INSERT INTO partidas (usuario_id, tipo_jogo, pontuacao, data_partida, resultado) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, 
                partida.getUsuarioId(), 
                partida.getTipoJogo(), 
                partida.getPontuacao(), 
                partida.getDataPartida(), 
                partida.getResultado());
        } catch (DataAccessException e) {
            System.err.println("❌ Erro ao salvar partida: " + e.getMessage());
        }
    }

    public List<Partida> buscarPorUsuario(int usuarioId) {
        try {
            String sql = "SELECT * FROM partidas WHERE usuario_id = ? ORDER BY data_partida DESC";
            return jdbcTemplate.query(sql, new PartidaRowMapper(), usuarioId);
        } catch (DataAccessException e) {
            System.err.println("❌ Erro ao buscar partidas por usuário: " + e.getMessage());
            return List.of(); // Retorna lista vazia em caso de erro
        }
    }

    private static class PartidaRowMapper implements RowMapper<Partida> {
        @Override
        public Partida mapRow(ResultSet rs, int rowNum) throws SQLException {
            Partida partida = new Partida();
            partida.setId(rs.getInt("id"));
            partida.setUsuarioId(rs.getInt("usuario_id"));
            partida.setTipoJogo(rs.getString("tipo_jogo"));
            partida.setPontuacao(rs.getInt("pontuacao"));
            
            // Converter java.sql.Timestamp para LocalDateTime
            java.sql.Timestamp timestamp = rs.getTimestamp("data_partida");
            if (timestamp != null) {
                partida.setDataPartida(timestamp.toLocalDateTime());
            }
            
            partida.setResultado(rs.getString("resultado"));
            return partida;
        }
    }
}