package com.server.nvp_server.dto

import com.server.nvp_server.model.Permission

data class UserDTO(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val permissions: MutableList<String>
)
