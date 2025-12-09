package com.server.nvp_server.helpers

object AuthContext {
    private val userData = ThreadLocal<AuthUser>()

    fun set(user: AuthUser?) = userData.set(user)
    fun get(): AuthUser? = userData.get()
    fun clear() = userData.remove()
}

data class AuthUser(
    val email: String,
    val permissions: List<String>
)
