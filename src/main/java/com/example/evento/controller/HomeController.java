package com.example.evento.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Muestra la pantalla de login
     */
    @GetMapping("/")
    public String login() {
        return "redirect:/login";
    }

}