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

import java.util.Random;

@Controller
public class AdivinhacaoController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private PartidaDAO partidaDAO;

    @GetMapping("/adivinhacao")
    public String adivinhacao(Model model) {
        System.out.println("🎯 GET /adivinhacao - Iniciando jogo");
        
        if (LoginController.getUsuarioLogado() == null) {
            return "redirect:/";
        }

        // Sempre inicia um novo jogo quando acessa a página
        iniciarNovoJogo(model);
        return "adivinhacao";
    }

    @PostMapping("/adivinhacao/tentar")
    public String tentar(@RequestParam("palpite") String palpiteStr,
                        @RequestParam(value = "numeroSecreto", required = false) String numeroSecretoStr,
                        @RequestParam(value = "tentativas", required = false) String tentativasStr,
                        Model model) {
        
        if (LoginController.getUsuarioLogado() == null) {
            return "redirect:/";
        }

        // Validações básicas para evitar erros
        int palpite;
        int numeroSecreto;
        int tentativas;
        
        try {
            palpite = Integer.parseInt(palpiteStr);
            numeroSecreto = Integer.parseInt(numeroSecretoStr);
            tentativas = Integer.parseInt(tentativasStr);
        } catch (NumberFormatException e) {
            // Se houver erro na conversão, inicia novo jogo
            iniciarNovoJogo(model);
            model.addAttribute("mensagem", "⚠️ Erro nos dados do jogo. Novo jogo iniciado!");
            return "adivinhacao";
        }

        String mensagem;
        boolean acertou = false;
        int pontuacao = 0;
        boolean jogoAtivo = true;

        // Validar range do palpite
        if (palpite < 1 || palpite > 100) {
            mensagem = "⚠️ Digite um número entre 1 e 100!";
        } else {
            tentativas++;

            if (palpite == numeroSecreto) {
                acertou = true;
                jogoAtivo = false;
                pontuacao = Math.max(100 - (tentativas * 5), 10);
                mensagem = "🎉 Parabéns! Você acertou em " + tentativas + " tentativas! +" + pontuacao + " pontos";
                registrarPontuacao(pontuacao, mensagem);
            } else if (palpite < numeroSecreto) {
                mensagem = "📈 Muito baixo! Tente um número maior.";
            } else {
                mensagem = "📉 Muito alto! Tente um número menor.";
            }
        }

        // Adicionar atributos ao modelo
        model.addAttribute("usuarioLogado", LoginController.getUsuarioLogado());
        model.addAttribute("titulo", "Areninha - Adivinhe o Número");
        model.addAttribute("mensagem", mensagem);
        model.addAttribute("tentativas", tentativas);
        model.addAttribute("jogoAtivo", jogoAtivo);
        model.addAttribute("acertou", acertou);
        model.addAttribute("ultimoPalpite", palpite);
        model.addAttribute("numeroSecreto", numeroSecreto); // Mantém o mesmo número secreto
        
        return "adivinhacao";
    }

    @GetMapping("/adivinhacao/novo")
    public String novoJogo() {
        return "redirect:/adivinhacao";
    }

    @PostMapping("/adivinhacao/dica")
    public String dica(@RequestParam("numeroSecreto") String numeroSecretoStr,
                      @RequestParam(value = "tentativas", required = false) String tentativasStr,
                      Model model) {
        
        if (LoginController.getUsuarioLogado() == null) {
            return "redirect:/";
        }

        int numeroSecreto;
        int tentativas = 0;
        
        try {
            numeroSecreto = Integer.parseInt(numeroSecretoStr);
            if (tentativasStr != null) {
                tentativas = Integer.parseInt(tentativasStr);
            }
        } catch (NumberFormatException e) {
            iniciarNovoJogo(model);
            model.addAttribute("mensagem", "⚠️ Erro nos dados do jogo. Novo jogo iniciado!");
            return "adivinhacao";
        }

        String dica;
        if (numeroSecreto % 2 == 0) {
            dica = "💡 Dica: O número é PAR";
        } else {
            dica = "💡 Dica: O número é ÍMPAR";
        }
        
        // Manter o estado do jogo
        model.addAttribute("usuarioLogado", LoginController.getUsuarioLogado());
        model.addAttribute("titulo", "Areninha - Adivinhe o Número");
        model.addAttribute("mensagem", dica);
        model.addAttribute("tentativas", tentativas);
        model.addAttribute("jogoAtivo", true);
        model.addAttribute("acertou", false);
        model.addAttribute("numeroSecreto", numeroSecreto);
        
        return "adivinhacao";
    }

    private void iniciarNovoJogo(Model model) {
        Random random = new Random();
        int numeroSecreto = random.nextInt(100) + 1; // Número entre 1 e 100
        
        model.addAttribute("numeroSecreto", numeroSecreto);
        model.addAttribute("tentativas", 0);
        model.addAttribute("jogoAtivo", true);
        model.addAttribute("mensagem", "Estou pensando em um número entre 1 e 100. Tente adivinhar!");
        model.addAttribute("acertou", false);
        model.addAttribute("ultimoPalpite", null);
        model.addAttribute("usuarioLogado", LoginController.getUsuarioLogado());
        model.addAttribute("titulo", "Areninha - Adivinhe o Número");
        
        System.out.println("🔢 Novo jogo - Número secreto: " + numeroSecreto);
    }

    private void registrarPontuacao(int pontuacao, String mensagem) {
        try {
            if (pontuacao > 0 && usuarioDAO != null && LoginController.getUsuarioLogado() != null) {
                usuarioDAO.atualizarPontuacao(LoginController.getUsuarioLogado().getId(), pontuacao);
                
                Partida partida = new Partida(
                    LoginController.getUsuarioLogado().getId(),
                    "Adivinhação",
                    pontuacao,
                    mensagem
                );
                partidaDAO.salvar(partida);
                
                System.out.println("✅ Pontuação Adivinhação registrada: " + pontuacao + " pontos");
            }
        } catch (Exception e) {
            System.err.println("❌ Erro ao registrar pontuação: " + e.getMessage());
        }
    }
}