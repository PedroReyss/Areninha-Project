package com.cefsa.areninha.controller;

import com.cefsa.areninha.dao.UsuarioDAO;
import com.cefsa.areninha.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @GetMapping("/admin/usuarios")
    public String listarUsuarios(Model model) {
        if (LoginController.getUsuarioLogado() == null) {
            return "redirect:/";
        }
        
        model.addAttribute("usuarios", usuarioDAO.listarTodos());
        model.addAttribute("usuarioLogado", LoginController.getUsuarioLogado());
        model.addAttribute("titulo", "Areninha - Gerenciar Usuários");
        return "admin-usuarios";
    }

    @GetMapping("/admin/usuarios/editar")
    public String editarUsuario(@RequestParam int id, Model model) {
        if (LoginController.getUsuarioLogado() == null) {
            return "redirect:/";
        }

        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            model.addAttribute("usuarioLogado", LoginController.getUsuarioLogado());
            model.addAttribute("titulo", "Areninha - Editar Usuário");
            return "editar-usuario";
        }
        
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/admin/usuarios/atualizar")
    public String atualizarUsuario(@RequestParam int id,
                                  @RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam String nome,
                                  @RequestParam String email,
                                  Model model) {
        
        if (LoginController.getUsuarioLogado() == null) {
            return "redirect:/";
        }

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setNome(nome);
        usuario.setEmail(email);

        if (usuarioDAO.atualizarUsuario(usuario)) {
            model.addAttribute("sucesso", "Usuário atualizado com sucesso!");
        } else {
            model.addAttribute("erro", "Erro ao atualizar usuário!");
        }

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/admin/usuarios/excluir")
    public String excluirUsuario(@RequestParam int id, Model model) {
        if (LoginController.getUsuarioLogado() == null) {
            return "redirect:/";
        }

        // Impedir que o usuário exclua a si mesmo
        if (id == LoginController.getUsuarioLogado().getId()) {
            model.addAttribute("erro", "Você não pode excluir sua própria conta!");
            return "redirect:/admin/usuarios";
        }

        if (usuarioDAO.excluirUsuario(id)) {
            model.addAttribute("sucesso", "Usuário excluído com sucesso!");
        } else {
            model.addAttribute("erro", "Erro ao excluir usuário!");
        }

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/perfil")
    public String perfil(Model model) {
        if (LoginController.getUsuarioLogado() == null) {
            return "redirect:/";
        }

        model.addAttribute("usuario", LoginController.getUsuarioLogado());
        model.addAttribute("titulo", "Areninha - Meu Perfil");
        return "perfil";
    }

    @PostMapping("/perfil/atualizar")
    public String atualizarPerfil(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String nome,
                                 @RequestParam String email,
                                 Model model) {
        
        Usuario usuarioLogado = LoginController.getUsuarioLogado();
        if (usuarioLogado == null) {
            return "redirect:/";
        }

        // Verificar se o username já existe (excluindo o usuário atual)
        Usuario usuarioExistente = usuarioDAO.buscarPorUsername(username);
        if (usuarioExistente != null && usuarioExistente.getId() != usuarioLogado.getId()) {
            model.addAttribute("erro", "Username já está em uso!");
            model.addAttribute("usuario", usuarioLogado);
            return "perfil";
        }

        // Atualizar usuário
        usuarioLogado.setUsername(username);
        usuarioLogado.setPassword(password);
        usuarioLogado.setNome(nome);
        usuarioLogado.setEmail(email);

        if (usuarioDAO.atualizarUsuario(usuarioLogado)) {
            model.addAttribute("sucesso", "Perfil atualizado com sucesso!");
        } else {
            model.addAttribute("erro", "Erro ao atualizar perfil!");
        }

        model.addAttribute("usuario", usuarioLogado);
        return "perfil";
    }
}