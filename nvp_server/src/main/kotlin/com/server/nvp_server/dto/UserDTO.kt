package com.server.nvp_server.dto

data class UserDTO(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val permissions: List<String>
)
