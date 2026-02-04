package ru.urfu.spring_urfu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(a -> {
            a.requestMatchers("/index").permitAll();
            a.requestMatchers("/users").hasRole("ADMIN");
            a.requestMatchers("/register/**").permitAll();
            a.anyRequest().authenticated();
        }).formLogin(form -> {
            form.loginPage("/login");
            form.loginProcessingUrl("/login");
            form.defaultSuccessUrl("/users");
            form.permitAll();
        }).logout(l -> l.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll());
        return http.build();
    }
}
