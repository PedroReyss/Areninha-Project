package com.cefsa.areninha.dao;

import com.cefsa.areninha.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UsuarioDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void criarTabela() {
        try {
            String sql = """
                IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='usuarios' AND xtype='U')
                CREATE TABLE usuarios (
                    id INT IDENTITY(1,1) PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(50) NOT NULL,
                    nome VARCHAR(100),
                    email VARCHAR(100),
                    pontuacao_total INT DEFAULT 0
                )
                """;
            jdbcTemplate.execute(sql);
            System.out.println("✅ Tabela 'usuarios' criada ou já existente");
        } catch (DataAccessException e) {
            System.err.println("❌ Erro ao criar tabela usuarios: " + e.getMessage());
        }
    }

    public boolean salvar(Usuario usuario) {
        try {
            String sql = "INSERT INTO usuarios (username, password, nome, email) VALUES (?, ?, ?, ?)";
            int rows = jdbcTemplate.update(sql, 
                usuario.getUsername(), 
                usuario.getPassword(), 
                usuario.getNome(), 
                usuario.getEmail());
            return rows > 0;
        } catch (DataAccessException e) {
            System.err.println("❌ Erro ao salvar usuário: " + e.getMessage());
            return false;
        }
    }

    public Usuario buscarPorUsername(String username) {
        try {
            String sql = "SELECT * FROM usuarios WHERE username = ?";
            List<Usuario> usuarios = jdbcTemplate.query(sql, new UsuarioRowMapper(), username);
            return usuarios.isEmpty() ? null : usuarios.get(0);
        } catch (DataAccessException e) {
            System.err.println("❌ Erro ao buscar usuário por username: " + e.getMessage());
            return null;
        }
    }

    public Usuario buscarPorUsernameESenha(String username, String password) {
        try {
            String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
            List<Usuario> usuarios = jdbcTemplate.query(sql, new UsuarioRowMapper(), username, password);
            return usuarios.isEmpty() ? null : usuarios.get(0);
        } catch (DataAccessException e) {
            System.err.println("❌ Erro ao buscar usuário por username e senha: " + e.getMessage());
            return null;
        }
    }

    // Método para atualizar pontuação
    public void atualizarPontuacao(int usuarioId, int pontuacao) {
        try {
            String sql = "UPDATE usuarios SET pontuacao_total = pontuacao_total + ? WHERE id = ?";
            jdbcTemplate.update(sql, pontuacao, usuarioId);
            System.out.println("✅ Pontuação atualizada: +" + pontuacao + " para usuário ID: " + usuarioId);
        } catch (DataAccessException e) {
            System.err.println("❌ Erro ao atualizar pontuação: " + e.getMessage());
        }
    }

    // Método para buscar ranking
    public List<Usuario> buscarRanking() {
        try {
            String sql = "SELECT * FROM usuarios ORDER BY pontuacao_total DESC, username ASC";
            return jdbcTemplate.query(sql, new UsuarioRowMapper());
        } catch (DataAccessException e) {
            System.err.println("❌ Erro ao buscar ranking: " + e.getMessage());
            return List.of();
        }
    }

    // Método para buscar top N usuários
    public List<Usuario> buscarTopUsuarios(int limite) {
        try {
            String sql = "SELECT TOP " + limite + " * FROM usuarios ORDER BY pontuacao_total DESC, username ASC";
            return jdbcTemplate.query(sql, new UsuarioRowMapper());
        } catch (DataAccessException e) {
            System.err.println("❌ Erro ao buscar top usuários: " + e.getMessage());
            return List.of();
        }
    }

    private static class UsuarioRowMapper implements RowMapper<Usuario> {
        @Override
        public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getInt("id"));
            usuario.setUsername(rs.getString("username"));
            usuario.setPassword(rs.getString("password"));
            usuario.setNome(rs.getString("nome"));
            usuario.setEmail(rs.getString("email"));
            usuario.setPontuacaoTotal(rs.getInt("pontuacao_total"));
            return usuario;
        }
    }

    public void atualizarStreak(int usuarioId) {
    String sql = "UPDATE usuarios SET ultima_data_jogo = CURDATE(), " +
                 "current_streak = CASE " +
                 "WHEN ultima_data_jogo IS NULL THEN 1 " +
                 "WHEN ultima_data_jogo = CURDATE() THEN current_streak " +
                 "WHEN ultima_data_jogo = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN current_streak + 1 " +
                 "ELSE 1 " +
                 "END, " +
                 "total_streak = CASE " +
                 "WHEN ultima_data_jogo IS NULL THEN 1 " +
                 "WHEN ultima_data_jogo = CURDATE() THEN total_streak " +
                 "WHEN ultima_data_jogo = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN " +
                 "GREATEST(total_streak, current_streak + 1) " +
                 "ELSE GREATEST(total_streak, 1) " +
                 "END, " +
                 "recompensa_disponivel = CASE " +
                 "WHEN (current_streak + 1) >= 3 AND ultima_data_jogo = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN TRUE " +
                 "ELSE recompensa_disponivel " +
                 "END " +
                 "WHERE id = ?";
    
    jdbcTemplate.update(sql, usuarioId);
}

public void resetarStreak(int usuarioId) {
    String sql = "UPDATE usuarios SET current_streak = 0, recompensa_disponivel = FALSE WHERE id = ?";
    jdbcTemplate.update(sql, usuarioId);
}

public void marcarRecompensaUtilizada(int usuarioId) {
    String sql = "UPDATE usuarios SET recompensa_disponivel = FALSE WHERE id = ?";
    jdbcTemplate.update(sql, usuarioId);
}

// Adicione este método para buscar por username

}