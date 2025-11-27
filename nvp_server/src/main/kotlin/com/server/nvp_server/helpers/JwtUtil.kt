package com.server.nvp_server.helpers

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.*
import javax.crypto.SecretKey

object JwtUtil {
    private val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    fun generateToken(email: String): String =
        Jwts.builder().setSubject(email).setExpiration(Date(System.currentTimeMillis() + 1000*60*60))
            .signWith(key).compact()

    fun extractEmail(token: String): String =
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.subject

    fun isTokenValid(token: String) = try {
        !Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body.expiration.before(Date())
    } catch (e: Exception) { false }
}