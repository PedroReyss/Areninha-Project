package com.cefsa.areninha.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrosController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        System.out.println("⚠️  Rota não encontrada, redirecionando para home");
        return "redirect:/home";
    }
}