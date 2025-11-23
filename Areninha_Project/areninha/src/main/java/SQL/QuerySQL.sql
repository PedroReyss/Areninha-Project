---------------------------------------------------------
-- CRIAÇÃO DO BANCO (rode apenas se quiser criar do zero)
---------------------------------------------------------
IF NOT EXISTS (SELECT 1 FROM sys.databases WHERE name = 'areninha')
BEGIN
    CREATE DATABASE areninha;
END
GO

USE areninha;
GO

---------------------------------------------------------
-- TABELA USUÁRIOS
---------------------------------------------------------
IF NOT EXISTS (SELECT * FROM sys.objects WHERE name = 'usuarios' AND type = 'U')
BEGIN
    CREATE TABLE usuarios (
        id INT IDENTITY(1,1) PRIMARY KEY,
        username VARCHAR(50) UNIQUE NOT NULL,
        password VARCHAR(50) NOT NULL,
        nome VARCHAR(100),
        email VARCHAR(100),
        pontuacao_total INT DEFAULT 0,
        ultima_data_jogo DATE NULL,
        current_streak INT DEFAULT 0,
        total_streak INT DEFAULT 0,
        recompensa_disponivel BIT DEFAULT 0
    );
END
GO

---------------------------------------------------------
-- TABELA PARTIDAS
---------------------------------------------------------
IF NOT EXISTS (SELECT * FROM sys.objects WHERE name = 'partidas' AND type = 'U')
BEGIN
    CREATE TABLE partidas (
        id INT IDENTITY(1,1) PRIMARY KEY,
        usuario_id INT NOT NULL,
        tipo_jogo VARCHAR(50),
        pontuacao INT,
        data_partida DATETIME,
        resultado VARCHAR(255),
        FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
    );
END
GO

---------------------------------------------------------
-- DADOS INICIAIS (opcional)
---------------------------------------------------------

-- Inserir usuários somente se ainda não existirem
IF NOT EXISTS (SELECT 1 FROM usuarios WHERE username = 'admin')
BEGIN
    INSERT INTO usuarios (username, password, nome, email)
    VALUES ('admin', 'admin123', 'Administrador', 'admin@areninha.com');
END

IF NOT EXISTS (SELECT 1 FROM usuarios WHERE username = 'jogador1')
BEGIN
    INSERT INTO usuarios (username, password, nome, email)
    VALUES ('jogador1', '123456', 'Jogador Um', 'jogador1@email.com');
END
GO

-- Inserir partidas apenas se tabela estiver vazia
IF NOT EXISTS (SELECT 1 FROM partidas)
BEGIN
    INSERT INTO partidas (usuario_id, tipo_jogo, pontuacao, data_partida, resultado) VALUES
    (1, 'BATALHA_NAVAL', 250, GETDATE(), 'Vitória com 250 pontos'),
    (1, 'ACERTAR_PALAVRA', 80, GETDATE(), 'Palavra acertada: JAVA'),
    (2, 'JOGO_VELHA', 100, GETDATE(), 'Vitória do jogador');
END
GO
