package com.anderson.filmes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration      // classe de configuração Spring
@EnableWebSecurity  // ativa o Spring Security — questão 14
public class SecurityConfig {

    // ===== Bean 1: PasswordEncoder =====
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // BCrypt adiciona "salt" automático — cada hash é único
    }

    // ===== Bean 2: Usuários em memória — questão 14 =====
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        var admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin123"))
                // .password("{bcrypt}...") — mas assim o encode é na inicialização
                .roles("ADMIN")    // Spring adiciona "ROLE_" automaticamente
                .build();

        var visitante = User.builder()
                .username("visitante")
                .password(encoder.encode("vis123"))
                .roles("VISITANTE")
                .build();

        return new InMemoryUserDetailsManager(admin, visitante);
    }

    // ===== Bean 3: SecurityFilterChain — questão 15 (RBAC) =====
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // arquivos estáticos (css, imagens) — liberados para todos
                        .requestMatchers("/css/**", "/images/**", "/js/**").permitAll()
                        // página 404 estática — libera
                        .requestMatchers("/error/**").permitAll()
                        // ROLE_ADMIN: cadastro e exclusão
                        .requestMatchers("/cadastro", "/deletar").hasRole("ADMIN")
                        // qualquer outro endpoint exige autenticação
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        // gera o formulário de login padrão do Spring
                        .defaultSuccessUrl("/index", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .permitAll()
                );

        return http.build();
    }
}