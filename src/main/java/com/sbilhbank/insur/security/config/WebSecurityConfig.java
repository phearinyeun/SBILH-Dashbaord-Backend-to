package com.sbilhbank.insur.security.config;

import com.sbilhbank.insur.extras.constant.EnumConst;
import com.sbilhbank.insur.security.JwtAuthenticationEntryPoint;
import com.sbilhbank.insur.security.JwtUserDetailsService;
import com.sbilhbank.insur.security.filter.JwtRequestFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {
    private OpenLdapAuthenticationProvider openLdapAuthenticationProvider;
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtRequestFilter jwtRequestFilter;
    private JwtUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/authenticate/**",
                        "/actuator/**").permitAll()
                .antMatchers("/authenticate/me").authenticated()
                .antMatchers(
                        "/dashboard/**").hasAnyRole(EnumConst.Role.DASHBOARD.name())
                .anyRequest().hasRole(EnumConst.Role.ADMIN.name())
                .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(encoder());
        return authenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        return new ProviderManager(List.of(authProvider(), openLdapAuthenticationProvider));
    }

}
