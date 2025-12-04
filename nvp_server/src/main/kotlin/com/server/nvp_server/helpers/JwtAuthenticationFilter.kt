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
        val header = request.getHeader("Authorization")
        if (header != null && header.startsWith("Bearer ")) {
            val token = header.substring(7)
            try {
                val email = jwtUtil.extractEmail(token)
                if (jwtUtil.isTokenValid(token)) {
                    val authorities = jwtUtil.extractAuthorities(token).toMutableList()

                    // ➜ ako postoji ADMIN u JWT-u – dodaj Spring-ovu rolu
                    if ("ADMIN" in authorities) {
                        authorities.add("ADMIN")
                    }

                    val auth = UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        authorities.map { SimpleGrantedAuthority(it) }
                    )
                    SecurityContextHolder.getContext().authentication = auth
                }
            } catch (e: Exception) {
                // token invalidan – pusti da prođe, ali bez autentikacije
            }
        }
        chain.doFilter(request, response)
    }
}