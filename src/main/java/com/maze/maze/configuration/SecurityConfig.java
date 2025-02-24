package com.maze.maze.configuration;

import com.maze.maze.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements UserDetailsService {
  @Autowired
  JWTAuthFilter jwtAuthFilter;

  @Bean
  // authentication
  public UserDetailsService userDetailsService(PasswordEncoder encoder) {
    return new UserService();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @SuppressWarnings("removal")
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http)
      throws Exception {
    return http
        .csrf(AbstractHttpConfigurer::disable)
        .cors()
        .and()
        .authorizeHttpRequests(
            authorize -> authorize
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/v3/api-docs.yaml",
                    "/swagger-resources/**")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/image/**", "/css/**", "/js/**")
                .permitAll()
                // Api Tài khoản
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/account/list",
                    "/api/get-account")
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/account/create")
                .permitAll()
                .requestMatchers(
                    HttpMethod.POST,
                    "api/account/auth/google",
                    "/api/account/forgot-password/send-otp",
                    "/api/account/forgot-password/change")
                .permitAll()
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/account/edit-profile",
                    "/api/account/change-password")
                .hasAnyRole("ADMIN", "USER")
                // Api Admin
                .requestMatchers(HttpMethod.GET, "/api/admin/**")
                .hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/admin/**")
                .hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/admin/**")
                .hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/admin/**")
                .hasAnyRole("ADMIN")


                
                .requestMatchers(HttpMethod.GET, "/api/lien-he")
                .permitAll())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(
            jwtAuthFilter,
            UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(
        userDetailsService(passwordEncoder()));
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
        "Unimplemented method 'loadUserByUsername'");
  }
}
