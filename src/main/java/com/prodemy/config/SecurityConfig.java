package com.prodemy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.prodemy.services.UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserService userService;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers("/registration**", "/js/**", "/css/**", "/img/**").permitAll()
                .anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/getAllData", true)
                        .permitAll())
                .logout((logout) -> logout.invalidateHttpSession(true).clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
                        .permitAll());
        return http.build();
    }

    // @Bean
    // public AuthenticationManager authenticationManager() {
    //     DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    //     authProvider.setUserDetailsService(userService);
    //     authProvider.setPasswordEncoder(passwordEncoder());
    //     return new ProviderManager(authProvider);
    // }

    // @Bean
    // public UserDetailsService userDetailsService() {
    //     UserDetails userDetails = User.withDefaultPasswordEncoder()
    //             .username("username")
    //             .password("password")
    //             .roles("USER")
    //             .build();
    //     return new InMemoryUserDetailsManager(userDetails);
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
