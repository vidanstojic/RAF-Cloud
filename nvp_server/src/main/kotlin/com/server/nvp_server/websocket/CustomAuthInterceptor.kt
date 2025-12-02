package com.server.nvp_server.websocket

import com.server.nvp_server.helpers.JwtUtil // Import ostaje isti
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import java.security.Principal
import java.net.URI
class CustomUserPrincipal(private val email: String) : Principal {
    override fun getName(): String = email
}

@Component
class CustomAuthInterceptor(
    private val jwtUtil: JwtUtil
) : HandshakeInterceptor {

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        val uri: URI = request.uri
        val query = uri.query

        val token = query?.split("&")
            ?.map { it.split("=") }
            ?.find { it.size == 2 && it[0] == "token" }
            ?.get(1)

        if (token.isNullOrEmpty()) {
            println("WS: Token nije prosleđen.")
            response.setStatusCode(HttpStatus.UNAUTHORIZED)
            return false
        }

        return try {
            if (jwtUtil.isTokenValid(token)) {
                val email = jwtUtil.extractEmail(token)

                val principal = CustomUserPrincipal(email)
                attributes["user"] = principal

                println("WS: Konekcija odobrena za: $email")
                true
            } else {
                println("WS: Token nije validan.")
                response.setStatusCode(HttpStatus.FORBIDDEN)
                false
            }
        } catch (e: Exception) {
            println("WS: Greška pri validaciji tokena: ${e.message}")
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
    }
}