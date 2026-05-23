package com.example.evento.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.evento.repository.EventoRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final EventoRepository eventoRepository;

    public AdminController(
            EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    /**
     * DASHBOARD PRINCIPAL
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("totalEventos", eventoRepository.count());
        model.addAttribute("eventos", eventoRepository.findAll());

        return "admin/dashboard";
    }
}