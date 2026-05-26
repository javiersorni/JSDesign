package com.example.evento.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.evento.entity.Usuario;
import com.example.evento.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        if (usuarioRepository.findByUsername("admin") == null) {

            Usuario admin = new Usuario();

            admin.setNombre("Administrador");

            admin.setUsername("admin");

            admin.setPassword(
                    passwordEncoder.encode("admin123")
            );

            admin.setRol("ROLE_ADMIN");

            usuarioRepository.save(admin);

            System.out.println("=================================");
            System.out.println("ADMIN CREADO AUTOMÁTICAMENTE");
            System.out.println("usuario: admin");
            System.out.println("password: admin123");
            System.out.println("=================================");
        }
    }
}