package com.server.nvp_server.security

import com.server.nvp_server.helpers.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.http.HttpMethod
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtFilter: JwtAuthenticationFilter
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .cors { cors ->
                cors.configurationSource {
                    CorsConfiguration().apply {
                        allowCredentials = true
                        allowedOrigins = listOf("http://localhost:4200")
                        allowedHeaders = listOf("*")
                        allowedMethods = listOf("*")
                    }
                }
            }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.POST, "/api/users/loginuser").permitAll()
                it.requestMatchers("/**").hasAuthority("ADMIN")
                it.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                it.requestMatchers("/ws/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/api/users").hasAuthority("READING_USER")
                it.requestMatchers(HttpMethod.POST, "/api/machines").hasAuthority("CREATING_MACHINE")
                it.requestMatchers(HttpMethod.DELETE, "/api/machines/**").hasAuthority("DESTROYING_MACHINE")
                it.requestMatchers(HttpMethod.DELETE, "/api/error-logs/**").hasAuthority("READING_ERROR")
                it.anyRequest().authenticated()
            }
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()

    @Bean
    fun authManager(config: AuthenticationConfiguration): AuthenticationManager =
        config.authenticationManager
}