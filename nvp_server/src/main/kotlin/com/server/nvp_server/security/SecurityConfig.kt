package com.server.nvp_server.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.http.HttpMethod
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {

    /* CORS filter koji će Spring Security koristiti */
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration().apply {
            allowCredentials = true
            allowedOrigins = listOf("http://localhost:4200")
            allowedHeaders = listOf("*")
            allowedMethods = listOf("*")
        }
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }

    /* Da bude prvi u lancu */
    @Bean
    fun corsFilterRegistration(corsFilter: CorsFilter): FilterRegistrationBean<CorsFilter> =
        FilterRegistrationBean<CorsFilter>().apply {
            filter = corsFilter
            order = Ordered.HIGHEST_PRECEDENCE
        }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // 1. napravimo source
        val source = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration(
                "/**",
                CorsConfiguration().apply {
                    allowCredentials = true
                    allowedOrigins = listOf("http://localhost:4200")
                    allowedHeaders = listOf("*")
                    allowedMethods = listOf("*")
                }
            )
        }

        return http
            .cors { it.configurationSource(source) }   // <-- prosleđujemo instancu
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.POST, "/api/users/loginuser").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .build()
    }
}