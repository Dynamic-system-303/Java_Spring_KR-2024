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
import ru.urfu.spring_urfu.service.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailService userDetailsService;

    public SecurityConfig(CustomUserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(a -> {
            a.requestMatchers("/", "/index", "/login", "/register/**").permitAll();
            a.requestMatchers("/users").hasRole("ADMIN");
            a.requestMatchers("/list").hasAnyRole("ADMIN", "USER", "READ_ONLY");
            a.requestMatchers("/addStudentForm", "/saveStudent").hasAnyRole("ADMIN", "USER");
            a.requestMatchers("/deleteStudent").hasRole("ADMIN");
            a.anyRequest().authenticated();
//
//            a.requestMatchers("/login").permitAll();
//            a.requestMatchers("/register/**").permitAll();
//
//            a.requestMatchers("/users").hasRole("ADMIN");
//
//            a.requestMatchers("/list").hasAnyRole("ADMIN", "USER", "READ_ONLY");
//            a.requestMatchers("/addStudentForm", "/saveStudent").hasAnyRole("ADMIN", "USER");
//            a.requestMatchers("/deleteStudent").hasRole("ADMIN");
//            a.anyRequest().authenticated();
        }).formLogin(form -> {
            form.loginPage("/login");
            form.loginProcessingUrl("/login");
            form.defaultSuccessUrl("/index", true);
            form.permitAll();
        }).logout(l -> l.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll());
        return http.build();
    }
}
