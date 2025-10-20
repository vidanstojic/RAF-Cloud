package com.server.nvp_server.model

import jakarta.persistence.*

enum class Permission{
    READING_USER,
    CREATING_USER,
    UPDATE_USER
}
