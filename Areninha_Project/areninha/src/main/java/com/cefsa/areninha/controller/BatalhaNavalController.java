package com.cefsa.areninha.controller;

import com.cefsa.areninha.dao.UsuarioDAO;
import com.cefsa.areninha.model.*;
import com.cefsa.areninha.dao.PartidaDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Controller
public class BatalhaNavalController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private PartidaDAO partidaDAO;

    private Set<String> naviosJogadorPosicoes = new HashSet<>();
    private Set<String> naviosAdversarioPosicoes = new HashSet<>();
    private Set<String> ataquesJogador = new HashSet<>();
    private Set<String> ataquesAdversario = new HashSet<>();
    private Set<String> acertosJogador = new HashSet<>();
    private Set<String> acertosAdversario = new HashSet<>();
    private Random random = new Random();
    private boolean jogoAtivo = true;
    private String vencedor = "";


    @GetMapping("/batalhanaval")
    public String batalhaNaval(Model model) {
        System.out.println("🎮 GET /batalhanaval - Iniciando jogo");
        
        // Reiniciar estado do jogo
        reiniciarJogo();
        
        // Inicializar estado do jogo
        model.addAttribute("pontuacao", 0);
        model.addAttribute("tentativas", 0);
        model.addAttribute("naviosJogador", 5);
        model.addAttribute("naviosAdversario", 5);
        model.addAttribute("fasePosicionamento", true);
        model.addAttribute("jogoAtivo", jogoAtivo);
        model.addAttribute("vencedor", vencedor);
        model.addAttribute("naviosJogadorPosicoes", naviosJogadorPosicoes);
        model.addAttribute("ataquesJogador", ataquesJogador);
        model.addAttribute("ataquesAdversario", ataquesAdversario);
        model.addAttribute("acertosJogador", acertosJogador);
        model.addAttribute("acertosAdversario", acertosAdversario);
        model.addAttribute("titulo", "Areninha - Batalha Naval");
        
        return "batalha-naval";
    }

    @PostMapping("/batalhanaval/posicionar")
    public String posicionarNavio(@RequestParam int linha, 
                                 @RequestParam int coluna,
                                 Model model) {
        
        System.out.println("🎯 POST /batalhanaval/posicionar - Posição: (" + linha + "," + coluna + ")");
        
        String posicao = linha + "," + coluna;
        
        // Verificar se a posição já foi selecionada
        if (!naviosJogadorPosicoes.contains(posicao) && naviosJogadorPosicoes.size() < 5) {
            naviosJogadorPosicoes.add(posicao);
        }
        
        int naviosRestantes = 5 - naviosJogadorPosicoes.size();
        boolean fasePosicionamento = naviosRestantes > 0;
        
        // Se terminou o posicionamento, gerar navios adversários
        if (!fasePosicionamento) {
            gerarNaviosAdversarios();
        }
        
        model.addAttribute("naviosJogador", naviosRestantes);
        model.addAttribute("fasePosicionamento", fasePosicionamento);
        model.addAttribute("jogoAtivo", jogoAtivo);
        model.addAttribute("vencedor", vencedor);
        model.addAttribute("naviosJogadorPosicoes", naviosJogadorPosicoes);
        model.addAttribute("ataquesJogador", ataquesJogador);
        model.addAttribute("ataquesAdversario", ataquesAdversario);
        model.addAttribute("acertosJogador", acertosJogador);
        model.addAttribute("acertosAdversario", acertosAdversario);
        model.addAttribute("pontuacao", 0);
        model.addAttribute("tentativas", 0);
        model.addAttribute("naviosAdversario", 5);
        model.addAttribute("titulo", "Areninha - Batalha Naval");
        
        return "batalha-naval";
    }

    @PostMapping("/batalhanaval/atacar")
    public String atacar(@RequestParam int linha, 
                        @RequestParam int coluna,
                        @RequestParam(defaultValue = "0") int pontuacao,
                        @RequestParam(defaultValue = "0") int tentativas,
                        Model model) {
        
        System.out.println("🎯 POST /batalhanaval/atacar - Ataque: (" + linha + "," + coluna + ")");
        
        if (LoginController.getUsuarioLogado() == null) {
            return "redirect:/";
        }
        
        String posicao = linha + "," + coluna;
        String resultadoJogador = "";
        String resultadoAdversario = "";
        
        // Ataque do jogador
        if (!ataquesJogador.contains(posicao) && jogoAtivo) {
            ataquesJogador.add(posicao);
            int novasTentativas = tentativas + 1;
            int novaPontuacao = pontuacao;
            int novosNaviosAdversario = 5 - acertosJogador.size();
            
            // Verificar se acertou um navio adversário
            if (naviosAdversarioPosicoes.contains(posicao)) {
                acertosJogador.add(posicao);
                resultadoJogador = "🎯 Acertou um navio inimigo! +100 pontos";
                novaPontuacao += 100;
                novosNaviosAdversario = 5 - acertosJogador.size();
                
                if (novosNaviosAdversario <= 0) {
                    resultadoJogador = "🎉 Vitória! Você destruiu todos os navios inimigos! +500 pontos de bônus!";
                    novaPontuacao += 500;
                    jogoAtivo = false;
                    vencedor = "jogador";
                    
                    // Registrar pontuação no ranking
                    registrarPontuacao(novaPontuacao, "Vitória - Batalha Naval");
                }
            } else {
                resultadoJogador = "💦 Água! Tente novamente.";
            }
            
            // Ataque do adversário (IA)
            if (jogoAtivo) {
                ataqueAdversario();
                int naviosJogadorRestantes = 5 - acertosAdversario.size();
                
                if (naviosJogadorRestantes <= 0) {
                    resultadoAdversario = "💥 O adversário destruiu todos os seus navios!";
                    jogoAtivo = false;
                    vencedor = "adversario";
                    
                    // Registrar pontuação mesmo na derrota
                    registrarPontuacao(novaPontuacao, "Derrota - Batalha Naval");
                }
            }
            
            model.addAttribute("pontuacao", novaPontuacao);
            model.addAttribute("tentativas", novasTentativas);
            model.addAttribute("naviosAdversario", 5 - acertosJogador.size());
        } else {
            resultadoJogador = "⏳ Posição já atacada!";
        }
        
        model.addAttribute("resultadoJogador", resultadoJogador);
        model.addAttribute("resultadoAdversario", resultadoAdversario);
        model.addAttribute("jogoAtivo", jogoAtivo);
        model.addAttribute("vencedor", vencedor);
        model.addAttribute("fasePosicionamento", false);
        model.addAttribute("naviosJogadorPosicoes", naviosJogadorPosicoes);
        model.addAttribute("ataquesJogador", ataquesJogador);
        model.addAttribute("ataquesAdversario", ataquesAdversario);
        model.addAttribute("acertosJogador", acertosJogador);
        model.addAttribute("acertosAdversario", acertosAdversario);
        model.addAttribute("naviosJogador", 5 - acertosAdversario.size());
        model.addAttribute("titulo", "Areninha - Batalha Naval");
        
        return "batalha-naval";
    }

    // Método para registrar pontuação
    private void registrarPontuacao(int pontuacao, String resultado) {
        try {
            Usuario usuario = LoginController.getUsuarioLogado();
            if (usuario != null && pontuacao > 0) {
                // Atualizar pontuação total do usuário
                usuarioDAO.atualizarPontuacao(usuario.getId(), pontuacao);
                
                // Registrar partida no histórico
                Partida partida = new Partida(
                    usuario.getId(), 
                    "Batalha Naval", 
                    pontuacao, 
                    resultado
                );
                partidaDAO.salvar(partida);
                
                System.out.println("✅ Pontuação registrada: " + pontuacao + " para " + usuario.getUsername());
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao registrar pontuação: " + e.getMessage());
        }
    }

    @GetMapping("/batalhanaval/reiniciar")
    public String reiniciar() {
        System.out.println("🔄 GET /batalhanaval/reiniciar - Reiniciando jogo");
        return "redirect:/batalhanaval";
    }
    
    private void gerarNaviosAdversarios() {
        naviosAdversarioPosicoes.clear();
        while (naviosAdversarioPosicoes.size() < 5) {
            int linha = random.nextInt(8);
            int coluna = random.nextInt(8);
            String posicao = linha + "," + coluna;
            naviosAdversarioPosicoes.add(posicao);
        }
        System.out.println("🚢 Navios adversários: " + naviosAdversarioPosicoes);
    }
    
    private void ataqueAdversario() {
        // IA simples - ataque aleatório
        String posicao;
        do {
            int linha = random.nextInt(8);
            int coluna = random.nextInt(8);
            posicao = linha + "," + coluna;
        } while (ataquesAdversario.contains(posicao));
        
        ataquesAdversario.add(posicao);
        
        // Verificar se acertou navio do jogador
        if (naviosJogadorPosicoes.contains(posicao)) {
            acertosAdversario.add(posicao);
            System.out.println("🤖 Adversário acertou em: " + posicao);
        }
    }
    
    private void reiniciarJogo() {
        naviosJogadorPosicoes.clear();
        naviosAdversarioPosicoes.clear();
        ataquesJogador.clear();
        ataquesAdversario.clear();
        acertosJogador.clear();
        acertosAdversario.clear();
        jogoAtivo = true;
        vencedor = "";
    }
}