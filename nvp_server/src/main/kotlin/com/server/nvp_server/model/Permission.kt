package com.server.nvp_server.model

import jakarta.persistence.*

enum class Permission{
    ADMIN,
    READING_USER,
    CREATING_USER,
    DELETING_USER,
    UPDATE_USER,
    DELETE_MACHINE,
    STARTING_MACHINE,
    CREATING_MACHINE,
    STOPPING_MACHINE,
    RESTARTING_MACHINE,
    DESTROYING_MACHINE,
    READING_ERROR,
    READING_MACHINE,
    SCHEDULING_MACHINE
}
