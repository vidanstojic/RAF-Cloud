package com.server.nvp_server.model

import jakarta.persistence.*

enum class Permission{
    READING_USER,
    CREATING_USER,
    DELETING_USER,
    UPDATE_USER,
    CREATE_MACHINE,
    DELETE_MACHINE,
    UPDATE_MACHINE,
    MACHINE_OPERATION
}
