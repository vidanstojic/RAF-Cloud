package com.server.nvp_server.model

import jakarta.persistence.*

@Entity
@Table(name = "permissions")
data class Permission(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val name: String
)
