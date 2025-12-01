package com.server.nvp_server.helpers

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
private val jwtUtil: JwtUtil
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        println(">>> JWT filter entered")
        val header = request.getHeader("Authorization")
        println(">>> Auth header: $header")
        if (header != null && header.startsWith("Bearer ")) {
            val token = header.substring(7)
            println(">>> Token: $token")
            try {
                val email = jwtUtil.extractEmail(token)
                println(">>> Email from token: $email")
                if (jwtUtil.isTokenValid(token)) {
                    val authorities = jwtUtil.extractAuthorities(token)
                    println(">>> Authorities from token: $authorities")
                    val auth = UsernamePasswordAuthenticationToken(
                        email, null, authorities.map { SimpleGrantedAuthority(it) }
                    )
                    SecurityContextHolder.getContext().authentication = auth
                    println(">>> Authentication set: ${SecurityContextHolder.getContext().authentication}")
                }
            } catch (e: Exception) {
                println(">>> Token error: ${e.message}")
            }
        }
        chain.doFilter(request, response)
    }
}