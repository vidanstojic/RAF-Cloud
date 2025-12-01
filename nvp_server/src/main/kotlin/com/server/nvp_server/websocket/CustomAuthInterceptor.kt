package com.server.nvp_server.websocket

import com.server.nvp_server.helpers.JwtUtil // Import vašeg utility-ja
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import java.security.Principal
import java.net.URI

// Jednostavna klasa koja čuva identitet korisnika
class CustomUserPrincipal(private val email: String) : Principal {
    override fun getName(): String = email
}

@Component
class CustomAuthInterceptor : HandshakeInterceptor {

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        // 1. Ekstrakcija tokena iz URL-a (npr. ?token=ey...)
        val uri: URI = request.uri
        val query = uri.query

        val token = query?.split("&")
            ?.map { it.split("=") }
            ?.find { it.size == 2 && it[0] == "token" }
            ?.get(1)

        // Ako nema tokena, odbij pristup
        if (token.isNullOrEmpty()) {
            println("❌ WS: Token nije prosleđen.")
            response.setStatusCode(HttpStatus.UNAUTHORIZED)
            return false
        }

        // 2. Validacija tokena koristeći vaš JwtUtil
        // Ovo menja Spring Security validaciju vašom custom logikom
        return try {
            if (JwtUtil.isTokenValid(token)) {
                val email = JwtUtil.extractEmail(token)

                // Kreiramo naš Principal objekat
                val principal = CustomUserPrincipal(email)

                // Čuvamo korisnika u sesiji
                attributes["user"] = principal

                println("✅ WS: Konekcija odobrena za: $email")
                true
            } else {
                println("❌ WS: Token nije validan.")
                response.setStatusCode(HttpStatus.FORBIDDEN)
                false
            }
        } catch (e: Exception) {
            println("❌ WS: Greška pri validaciji tokena: ${e.message}")
            response.setStatusCode(HttpStatus.FORBIDDEN)
            false
        }
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
        // Nije potrebno
    }
}