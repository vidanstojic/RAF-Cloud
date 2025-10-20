package com.server.nvp_server.model

import jakarta.persistence.*

enum class Permission{
    ADMIN,
    EDIT,    // was CREATING_USER
    VIEW     // was UPDATE_USER
}
