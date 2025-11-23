package com.cefsa.areninha.controller;

import com.cefsa.areninha.dao.UsuarioDAO;
import com.cefsa.areninha.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class RankingController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @GetMapping("/ranking")
    public String ranking(Model model) {
        System.out.println("🏆 GET /ranking - Tentando exibir ranking");
        
        // Verificação básica de login
        if (LoginController.getUsuarioLogado() == null) {
            System.out.println("❌ Usuário não logado");
            return "redirect:/";
        }
        
        try {
            System.out.println("📊 Buscando usuários para ranking...");
            List<Usuario> usuarios = usuarioDAO.buscarRanking();
            System.out.println("✅ Usuários encontrados: " + usuarios.size());
            
            // Adiciona apenas os dados essenciais
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("usuarioLogado", LoginController.getUsuarioLogado());
            
            // Calcula posição de forma mais simples
            int posicao = calcularPosicao(usuarios, LoginController.getUsuarioLogado().getId());
            model.addAttribute("posicaoUsuario", posicao);
            System.out.println("🎯 Posição do usuário: " + posicao);
            
            return "ranking";
            
        } catch (Exception e) {
            System.err.println("💥 ERRO no ranking: " + e.getMessage());
            e.printStackTrace();
            // Em caso de erro, redireciona para home
            return "redirect:/home";
        }
    }

    @GetMapping("/ranking/top")
    public String rankingTop(Model model) {
        System.out.println("🏆 GET /ranking/top");
        
        if (LoginController.getUsuarioLogado() == null) {
            return "redirect:/";
        }
        
        try {
            List<Usuario> usuarios = usuarioDAO.buscarTopUsuarios(10);
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("usuarioLogado", LoginController.getUsuarioLogado());
            
            int posicao = calcularPosicao(usuarios, LoginController.getUsuarioLogado().getId());
            model.addAttribute("posicaoUsuario", posicao);
            
            return "ranking";
            
        } catch (Exception e) {
            System.err.println("💥 ERRO no ranking top: " + e.getMessage());
            return "redirect:/home";
        }
    }

    // Método simplificado e mais seguro
    private int calcularPosicao(List<Usuario> usuarios, long usuarioId) {
        if (usuarios == null || usuarios.isEmpty()) {
            return -1;
        }
        
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            if (usuario != null && usuario.getId() == usuarioId) {
                return i + 1;
            }
        }
        return -1;
    }
}