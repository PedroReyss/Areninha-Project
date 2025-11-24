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
- **Banco de Dados:** MySQL (via Spring Data JPA)  
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

-> Crie um banco MySQL chamado areninha

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
├── dao/
└── resources/
    ├── templates/
    └── static/

