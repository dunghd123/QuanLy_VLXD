package com.example.quanly_vlxd.config;

import com.example.quanly_vlxd.enums.RoleEnums;
import com.example.quanly_vlxd.jwt.JwtAuthenticationFilter;
import com.example.quanly_vlxd.service.impl.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailServiceImpl userDetailService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(cors-> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth-> auth
                        .requestMatchers( "/swagger-ui/**",
                        "/v3/api-docs/**",      // Đường dẫn cho OpenAPI
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**").permitAll()
                        .requestMatchers("/api/v1/user/**").permitAll()
                        .requestMatchers("/api/v1/supplier/**").hasAnyAuthority(RoleEnums.MANAGER.name(),RoleEnums.EMPLOYEE.name())
                        .requestMatchers("/api/v1/customer/**").hasAnyAuthority(RoleEnums.EMPLOYEE.name(),RoleEnums.MANAGER.name())
                        .requestMatchers("/api/v1/product/**").hasAnyAuthority(RoleEnums.EMPLOYEE.name(), RoleEnums.MANAGER.name())
                        .requestMatchers("/api/v1/price-history/**").hasAnyAuthority(RoleEnums.MANAGER.name(), RoleEnums.EMPLOYEE.name())
                        .requestMatchers("/api/v1/employee/**").hasAnyAuthority(RoleEnums.MANAGER.name(),RoleEnums.EMPLOYEE.name())
                        .requestMatchers("/api/v1/category/**").hasAnyAuthority(RoleEnums.MANAGER.name())
                        .requestMatchers("/api/v1/warehouse/**").hasAnyAuthority(RoleEnums.MANAGER.name(), RoleEnums.EMPLOYEE.name())
                        .requestMatchers(("/api/v1/input-invoice/**")).hasAnyAuthority(RoleEnums.EMPLOYEE.name(),RoleEnums.MANAGER.name())
                        .requestMatchers("/api/v1/output-invoice/**").hasAnyAuthority(RoleEnums.EMPLOYEE.name(),RoleEnums.MANAGER.name())
                        .requestMatchers("/api/v1/sales-reports/**").hasAnyAuthority(RoleEnums.MANAGER.name(),RoleEnums.EMPLOYEE.name())
                        .requestMatchers("/api/v1/wh-pro-service/**").hasAnyAuthority(RoleEnums.MANAGER.name())
                        .anyRequest().authenticated())
                .userDetailsService(userDetailService)
                .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // FE Angular
        configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailServiceImpl();
    }

}
