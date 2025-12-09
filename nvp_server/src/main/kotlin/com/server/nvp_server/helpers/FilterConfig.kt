package com.server.nvp_server.helpers

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.boot.web.servlet.FilterRegistrationBean

@Configuration
class FilterConfig {

    @Bean
    fun jwtFilterRegistration(jwtFilter: JwtAuthenticationFilter): FilterRegistrationBean<JwtAuthenticationFilter> {
        val registration = FilterRegistrationBean<JwtAuthenticationFilter>()
        registration.filter = jwtFilter
        registration.addUrlPatterns("/*")
        registration.order = 1
        return registration
    }
}
