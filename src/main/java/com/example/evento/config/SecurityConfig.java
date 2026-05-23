package com.example.evento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http

                                // =========================
                                // AUTORIZACIÓN
                                // =========================
                                .authorizeHttpRequests(auth -> auth

                                                // Login público
                                                .requestMatchers("/login").permitAll()

                                                .requestMatchers("/info/**").permitAll()
                                                // Recursos estáticos
                                                .requestMatchers(
                                                                "/css/**",
                                                                "/js/**",
                                                                "/img/**")
                                                .permitAll()

                                                // Zona admin protegida
                                                .requestMatchers("/admin/**")
                                                .hasRole("ADMIN")

                                                // Cualquier otra requiere login
                                                .anyRequest()
                                                .authenticated())

                                // =========================
                                // LOGIN
                                // =========================
                                .formLogin(form -> form

                                                .loginPage("/login")

                                                // username y password por defecto
                                                .defaultSuccessUrl("/admin/dashboard", true)

                                                .permitAll())

                                // =========================
                                // LOGOUT
                                // =========================
                                .logout(logout -> logout

                                                .logoutUrl("/logout")

                                                .logoutSuccessUrl("/login?logout")

                                                // invalida sesión
                                                .invalidateHttpSession(true)

                                                // limpia autenticación
                                                .clearAuthentication(true)

                                                // elimina cookie sesión
                                                .deleteCookies("JSESSIONID")

                                                .permitAll())

                                // =========================
                                // SEGURIDAD EXTRA
                                // =========================
                                .sessionManagement(session -> session
                                                .invalidSessionUrl("/login"))

                                // =========================
                                // CSRF
                                // =========================
                                .csrf(csrf -> csrf.disable());

                return http.build();
        }

        // =========================
        // PASSWORD ENCODER
        // =========================
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}