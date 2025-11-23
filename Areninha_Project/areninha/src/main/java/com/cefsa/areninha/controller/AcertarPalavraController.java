package com.cefsa.areninha.controller;

import com.cefsa.areninha.dao.UsuarioDAO;
import com.cefsa.areninha.dao.PartidaDAO;
import com.cefsa.areninha.model.Partida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
public class AcertarPalavraController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private PartidaDAO partidaDAO;

    // Banco de palavras e dicas - usando array simples
    private final String[][] BANCO_PALAVRAS = {
        {"JAVA", "Linguagem de programação orientada a objetos"},
        {"PYTHON", "Linguagem de programação conhecida por sua simplicidade"},
        {"HTML", "Linguagem de marcação para páginas web"},
        {"CSS", "Linguagem para estilizar páginas web"},
        {"MYSQL", "Sistema de gerenciamento de banco de dados relacional"},
        {"SPRING", "Framework popular para aplicações Java"},
        {"JAVASCRIPT", "Linguagem de programação para web"},
        {"REACT", "Biblioteca JavaScript para interfaces"},
        {"DOCKER", "Plataforma para containerização"},
        {"GITHUB", "Plataforma de hospedagem de código"}
    };

    // Estado do jogo com inicialização padrão
    private String palavraAtual = "";
    private String palavraOcultaAtual = "";
    private String dicaAtual = "";
    private int tentativasRestantes = 6;
    private int pontuacaoAtual = 0;
    private Set<String> letrasTentadas = new HashSet<>();
    private boolean jogoTerminado = false;
    private String mensagemAtual = "🎯 Tente adivinhar a palavra! Digite uma letra.";

    @GetMapping("/acertarpalavra")
    public String acertarPalavra(Model model) {
        System.out.println("🎮 GET /acertarpalavra - Iniciando novo jogo");
        
        // Verificar login
        if (LoginController.getUsuarioLogado() == null) {
            System.out.println("❌ Usuário não logado, redirecionando para login");
            return "redirect:/";
        }

        // Iniciar novo jogo
        iniciarNovoJogo();

        // Adicionar atributos ao modelo
        adicionarAtributosModelo(model);
        model.addAttribute("usuarioLogado", LoginController.getUsuarioLogado());
        model.addAttribute("titulo", "Areninha - Acertar Palavra");
        
        System.out.println("✅ Jogo iniciado - Palavra: " + palavraAtual);
        return "acertar-palavra";
    }

    @PostMapping("/acertarpalavra/jogar")
    public String jogar(@RequestParam("letra") String letra, Model model) {
        System.out.println("🎯 POST /acertarpalavra/jogar - Letra: " + letra);
        
        // Verificar login
        if (LoginController.getUsuarioLogado() == null) {
            return "redirect:/";
        }

        // Se não há jogo ativo, iniciar um novo
        if (palavraAtual == null || palavraAtual.isEmpty()) {
            iniciarNovoJogo();
        }

        // Processar a jogada
        if (letra != null && !letra.trim().isEmpty()) {
            processarJogada(letra.trim().toUpperCase());
        } else {
            mensagemAtual = "⚠️ Por favor, digite uma letra!";
        }

        // Verificar se o jogo terminou
        if (jogoTerminado) {
            registrarPontuacao();
        }

        // Adicionar atributos ao modelo
        adicionarAtributosModelo(model);
        model.addAttribute("usuarioLogado", LoginController.getUsuarioLogado());
        model.addAttribute("titulo", "Areninha - Acertar Palavra");
        
        return "acertar-palavra";
    }

    @GetMapping("/acertarpalavra/reiniciar")
    public String reiniciar() {
        System.out.println("🔄 GET /acertarpalavra/reiniciar - Reiniciando jogo");
        // Resetar estado para forçar novo jogo no próximo GET
        palavraAtual = "";
        return "redirect:/acertarpalavra";
    }

    private void iniciarNovoJogo() {
        Random random = new Random();
        int indice = random.nextInt(BANCO_PALAVRAS.length);
        String[] palavraDica = BANCO_PALAVRAS[indice];
        
        palavraAtual = palavraDica[0];
        dicaAtual = palavraDica[1];
        palavraOcultaAtual = gerarPalavraOculta(palavraAtual);
        tentativasRestantes = 6;
        pontuacaoAtual = 0;
        letrasTentadas.clear();
        jogoTerminado = false;
        mensagemAtual = "🎯 Tente adivinhar a palavra! Digite uma letra.";
    }

    private String gerarPalavraOculta(String palavra) {
        StringBuilder oculta = new StringBuilder();
        for (int i = 0; i < palavra.length(); i++) {
            oculta.append("_");
        }
        return oculta.toString();
    }

    private void processarJogada(String letra) {
        // Validar entrada
        if (letra == null || letra.length() != 1 || !Character.isLetter(letra.charAt(0))) {
            mensagemAtual = "⚠️ Por favor, digite uma única letra válida!";
            return;
        }

        if (jogoTerminado) {
            mensagemAtual = "⚠️ O jogo já terminou! Clique em 'Novo Jogo' para jogar novamente.";
            return;
        }

        if (letrasTentadas.contains(letra)) {
            mensagemAtual = "⚠️ Letra '" + letra + "' já foi tentada!";
            return;
        }

        letrasTentadas.add(letra);

        if (palavraAtual.contains(letra)) {
            // Letra correta
            mensagemAtual = "🎉 Letra '" + letra + "' encontrada! +10 pontos";
            pontuacaoAtual += 10;
            
            // Revelar letra na palavra oculta
            char[] ocultaArray = palavraOcultaAtual.toCharArray();
            boolean palavraCompleta = true;
            
            for (int i = 0; i < palavraAtual.length(); i++) {
                if (palavraAtual.charAt(i) == letra.charAt(0)) {
                    ocultaArray[i] = letra.charAt(0);
                }
                if (ocultaArray[i] == '_') {
                    palavraCompleta = false;
                }
            }
            palavraOcultaAtual = new String(ocultaArray);
            
            // Verificar se acertou a palavra completa
            if (palavraCompleta) {
                mensagemAtual = "🎉🎉 PARABÉNS! Você acertou a palavra '" + palavraAtual + "'! +100 pontos bônus!";
                pontuacaoAtual += 100;
                jogoTerminado = true;
            }
        } else {
            // Letra errada
            tentativasRestantes--;
            mensagemAtual = "💦 Letra '" + letra + "' não encontrada. Tentativas restantes: " + tentativasRestantes;
            
            if (tentativasRestantes <= 0) {
                mensagemAtual = "💥 Fim de jogo! A palavra era: '" + palavraAtual + "'";
                jogoTerminado = true;
            }
        }
    }

    private void adicionarAtributosModelo(Model model) {
        model.addAttribute("palavra", palavraAtual != null ? palavraAtual : "");
        model.addAttribute("palavraOculta", formatarPalavraOculta(palavraOcultaAtual));
        model.addAttribute("dica", dicaAtual != null ? dicaAtual : "");
        model.addAttribute("tentativas", tentativasRestantes);
        model.addAttribute("pontuacao", pontuacaoAtual);
        model.addAttribute("resultado", mensagemAtual != null ? mensagemAtual : "");
        model.addAttribute("jogoAtivo", !jogoTerminado);
        model.addAttribute("letrasTentadas", letrasTentadas);
    }

    private String formatarPalavraOculta(String palavraOculta) {
        if (palavraOculta == null || palavraOculta.isEmpty()) {
            return "";
        }
        StringBuilder formatada = new StringBuilder();
        for (char c : palavraOculta.toCharArray()) {
            formatada.append(c).append(" ");
        }
        return formatada.toString().trim();
    }

    private void registrarPontuacao() {
        try {
            if (pontuacaoAtual > 0 && usuarioDAO != null && LoginController.getUsuarioLogado() != null) {
                usuarioDAO.atualizarPontuacao(LoginController.getUsuarioLogado().getId(), pontuacaoAtual);
                
                if (partidaDAO != null) {
                    Partida partida = new Partida(
                        LoginController.getUsuarioLogado().getId(),
                        "Acertar Palavra",
                        pontuacaoAtual,
                        mensagemAtual
                    );
                    partidaDAO.salvar(partida);
                }
                
                System.out.println("✅ Pontuação registrada: " + pontuacaoAtual + " pontos para " + 
                                 LoginController.getUsuarioLogado().getUsername());
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao registrar pontuação: " + e.getMessage());
        }
    }
}