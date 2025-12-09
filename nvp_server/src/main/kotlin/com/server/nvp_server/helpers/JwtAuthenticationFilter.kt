package com.server.nvp_server.helpers

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil
) : Filter {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        println("JWT FILTER HIT")

        val httpReq = request as HttpServletRequest
        val header = httpReq.getHeader("Authorization")


        if (header != null && header.startsWith("Bearer ")) {
            val token = header.substring(7)
            try {
                if (jwtUtil.isTokenValid(token)) {
                    val email = jwtUtil.extractEmail(token)
                    val permissions = jwtUtil.extractAuthorities(token)

                    AuthContext.set(AuthUser(email, permissions))
                }
            } catch (e: Exception) {
                println("INVALID TOKEN")
            }
        }

        try {
            chain.doFilter(request, response)
        } finally {
            AuthContext.clear()
        }
    }
}
