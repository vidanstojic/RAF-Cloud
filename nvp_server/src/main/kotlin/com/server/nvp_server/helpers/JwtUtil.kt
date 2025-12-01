package com.server.nvp_server.helpers

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil {
    private val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    /* bez autoriteta – za filter */
    fun generateToken(email: String): String =
        Jwts.builder()
            .setSubject(email)
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(key)
            .compact()

    /* SA autoritetima – za login */
    fun generateToken(email: String, authorities: List<String>): String =
        Jwts.builder()
            .setSubject(email)
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .claim("authorities", authorities)
            .signWith(key)
            .compact()

    /* ostali metodi ostaju isti */
    fun extractEmail(token: String): String =
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.subject

    fun extractAuthorities(token: String): List<String> =
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body["authorities"] as List<String>

    fun isTokenValid(token: String): Boolean = try {
        !Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.expiration.before(Date())
    } catch (e: Exception) { false }
}