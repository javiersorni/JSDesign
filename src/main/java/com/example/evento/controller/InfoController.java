package com.example.evento.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController { 
    
    @GetMapping("/info")
    public String servicio(){
        return "info/servicios";
    }

    @GetMapping("/info/compra")
    public String compra(){
        return "info/comprar";
    } 
}
