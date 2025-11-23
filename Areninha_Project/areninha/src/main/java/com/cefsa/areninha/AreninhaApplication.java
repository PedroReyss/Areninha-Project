package com.cefsa.areninha;

import com.cefsa.areninha.dao.UsuarioDAO;
import com.cefsa.areninha.dao.PartidaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;


@SpringBootApplication
public class AreninhaApplication {

    @Autowired
    private UsuarioDAO usuarioDAO;
    
    @Autowired
    private PartidaDAO partidaDAO;

    public static void main(String[] args) {
        System.out.println("🚀 Iniciando aplicação Areninha...");
        SpringApplication.run(AreninhaApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void iniciar() {
        try {
            // Criar tabelas ao iniciar a aplicação
            usuarioDAO.criarTabela();
            partidaDAO.criarTabela();
            
            // Criar usuário admin padrão se não existir
            if (usuarioDAO.buscarPorUsername("admin") == null) {
                com.cefsa.areninha.model.Usuario admin = new com.cefsa.areninha.model.Usuario();
                admin.setUsername("admin");
                admin.setPassword("admin123");
                admin.setNome("Administrador");
                admin.setEmail("admin@areninha.com");
                usuarioDAO.salvar(admin);
                System.out.println("👤 Usuário admin criado: admin / admin123");
            }
            
            System.out.println("✅ Aplicação Areninha iniciada com sucesso!");
            System.out.println("🌐 Acesse: http://localhost:8080");
            System.out.println("📊 Banco de dados configurado");
            
        

        } catch (Exception e) {
            System.err.println("❌ Erro ao iniciar aplicação: " + e.getMessage());
            e.printStackTrace();
        }
    }

   
}