CREATE DATABASE Areninha
GO

USE Areninha
GO

-- Tabela USUARIO
CREATE TABLE usuario (
                         id BIGINT PRIMARY KEY IDENTITY(1,1),
                         nick VARCHAR(15) UNIQUE NOT NULL,
                         email VARCHAR(100) UNIQUE NOT NULL,
                         senha VARCHAR(255) NOT NULL,
                         data_cadastro DATETIME2 DEFAULT GETDATE(),
                         data_ultima_aparicao DATETIME2,
                         nivel INT DEFAULT 1,
                         experiencia INT DEFAULT 0,
                         ativo BIT DEFAULT 1
);
GO

-- Tabela JOGO
CREATE TABLE jogo (
                      id BIGINT PRIMARY KEY IDENTITY(1,1),
                      nome VARCHAR(100) NOT NULL,
                      descricao TEXT,
                      icone VARCHAR(255)
);
GO

-- Tabela PARTIDA
CREATE TABLE partida (
                         id BIGINT PRIMARY KEY IDENTITY(1,1),
                         usuario_id BIGINT NOT NULL,
                         jogo_id BIGINT NOT NULL,
                         pontuacao INT NOT NULL DEFAULT 0,
                         nivel_alcancado INT DEFAULT 1,
                         tempo_jogo_segundos INT,
                         data_partida DATETIME2 DEFAULT GETDATE(),
                         concluida BIT DEFAULT 0,
                         FOREIGN KEY (usuario_id) REFERENCES usuario(id),
                         FOREIGN KEY (jogo_id) REFERENCES jogo(id)
);
GO