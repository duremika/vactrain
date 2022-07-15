package ru.duremika.vactrain.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import ru.duremika.vactrain.entities.User;
import ru.duremika.vactrain.services.UserService;

import java.util.Optional;


@Configuration
public class SecurityConfig {
    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }


    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> user = userService.findUser(username);
            if (user.isEmpty()) {
                throw new UsernameNotFoundException("No user found with username: " + username);
            }
            return user.get();
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/login", "/register")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                .defaultSuccessUrl("/authenticated")
                    .permitAll()
                    .and()
                .logout()
                    .permitAll();
        return http.build();
    }

}
