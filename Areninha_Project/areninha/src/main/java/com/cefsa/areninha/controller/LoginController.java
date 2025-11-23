package com.cefsa.areninha.controller;

import com.cefsa.areninha.model.Usuario;
import com.cefsa.areninha.dao.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    // Variável estática para simular sessão (simplificado)
    private static Usuario usuarioLogado = null;

    @GetMapping("/")
    public String paginaInicial() {
        System.out.println("🔍 Acessando página inicial de login");
        return "login";
    }

    @PostMapping("/login")
    public String processarLogin(@RequestParam String username, 
                               @RequestParam String password,
                               Model model) {
        
        System.out.println("🔍 Tentativa de login: " + username);
        
        Usuario usuario = usuarioDAO.buscarPorUsernameESenha(username, password);
        if (usuario != null) {
            System.out.println("✅ Login bem-sucedido para: " + username);
            usuarioLogado = usuario;
            // Adiciona o usuário ao modelo para a próxima view
            model.addAttribute("usuario", usuario);
            return "redirect:/home";
        } else {
            System.out.println("❌ Login falhou para: " + username);
            model.addAttribute("erro", "Usuário ou senha inválidos!");
            return "login";
        }
    }

    @GetMapping("/cadastro")
    public String paginaCadastro(Model model) {
        System.out.println("🔍 Acessando página de cadastro");
        model.addAttribute("titulo", "Areninha - Cadastro");
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String processarCadastro(@RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam String nome,
                                  @RequestParam String email,
                                  Model model) {
        
        System.out.println("🔍 Tentativa de cadastro: " + username);
        
        if (usuarioDAO.buscarPorUsername(username) != null) {
            model.addAttribute("erro", "Usuário já existe!");
            return "cadastro";
        }
        
        Usuario usuario = new Usuario(username, password, nome, email);
        boolean sucesso = usuarioDAO.salvar(usuario);
        
        if (sucesso) {
            model.addAttribute("sucesso", "Usuário cadastrado com sucesso! Faça login.");
            return "login";
        } else {
            model.addAttribute("erro", "Erro ao cadastrar usuário!");
            return "cadastro";
        }
    }
    @GetMapping("/logout")
    public String logout() {
        usuarioLogado = null;
        return "redirect:/";
    }

    // Método para verificar se há usuário logado
    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
}