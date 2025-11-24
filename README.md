# 🎮 Areninha — Plataforma de Jogos 

A **Areninha** é uma plataforma web interativa desenvolvida em **Java Spring Boot**, oferecendo uma coleção de jogos educativos com sistema de pontuação, ranking global e gerenciamento completo de usuários.

---
## 👥 Integrantes do Projeto

Pedro Henrique Simões Reys - 081230022

André Mende Garcia - 081230012

Vinicius Yamaguti Augusto - 081220040

---

## 🕹️ Jogos Disponíveis

### **1. Acertar Palavra**
- Jogo de forca com palavras relacionadas à tecnologia  
- Sistema de dicas e pontuação progressiva  
- Banco de palavras com termos de programação  

### **2. Adivinhação de Número**
- Adivinhe um número entre 1 e 100  
- Dicas inteligentes (par/ímpar)  
- Pontuação baseada nas tentativas  

### **3. Batalha Naval**
- Clássico jogo de estratégia naval  
- Tabuleiro 8x8 para posicionamento  
- Sistema de ataque alternado entre jogador e IA  

---

## 🚀 Funcionalidades

- **Autenticação** (login e cadastro)  
- **Perfil do Usuário** (edição de dados pessoais)  
- **Ranking Global** dos melhores jogadores  
- **Histórico de Partidas**  
- **Administração** de usuários (para contas admin)  

---

## 🛠️ Tecnologias Utilizadas

- **Backend:** Java Spring Boot  
- **Frontend:** Thymeleaf, HTML, CSS, JavaScript  
- **Banco de Dados:** SQL Server (via Spring Data JPA)  
- **Autenticação:** Sessão customizada  
- **Build:** Maven  

---

## 📋 Pré-requisitos

- **Java 17+**  
- **Maven 3.6+**  
- **MySQL 5.7+**  
- IDE como **IntelliJ**, **Eclipse** ou **VS Code**  

---

## 🚀 Como Executar

### 1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/areninha.git
cd areninha

```
---

## 🚀 Configure o banco de dados

-> Crie um banco SQL Server com as seguintes instruções:
```sql
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
```

-> Ajuste as credenciais em src/main/resources/application.properties

---

## 📁 Estrutura do Projeto

```bash
src/main/java/com/cefsa/areninha/
├── controller/
│   ├── LoginController.java
│   ├── HomeController.java
│   ├── AcertarPalavraController.java
│   ├── AdivinhacaoController.java
│   ├── BatalhaNavalController.java
│   ├── RankingController.java
│   ├── UsuarioController.java
│   └── ErrosController.java
├── model/
|     └── Usuario.java
|     └── Partida.java
├── dao/
|    └── UsuarioDAO.java
|    └── PartidaDAO.java
└── resources/
    ├── templates/
    └── static/

