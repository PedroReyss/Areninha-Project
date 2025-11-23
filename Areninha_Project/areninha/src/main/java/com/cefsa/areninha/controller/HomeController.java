package com.cefsa.areninha.controller;

import com.cefsa.areninha.dao.PartidaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private PartidaDAO partidaDAO;

    @GetMapping("/home")
    public String home(Model model) {
        // Verificar se usuário está logado
        if (LoginController.getUsuarioLogado() == null) {
            System.out.println("❌ Usuário não logado, redirecionando para login");
            return "redirect:/";
        }
        
        System.out.println("🔍 Acessando página home para: " + LoginController.getUsuarioLogado().getUsername());
        
        model.addAttribute("usuario", LoginController.getUsuarioLogado());
        model.addAttribute("partidas", partidaDAO.buscarPorUsuario(LoginController.getUsuarioLogado().getId()));
        model.addAttribute("titulo", "Areninha - Home");
        
        return "home";
    }
    
    
}
    
