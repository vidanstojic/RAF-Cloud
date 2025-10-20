package com.server.nvp_server.model

import jakarta.persistence.*

enum class Permission{
    ADMIN,   // was READING_USER
    EDIT,    // was CREATING_USER
    VIEW     // was UPDATE_USER
}
