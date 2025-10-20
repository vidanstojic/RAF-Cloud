package com.server.nvp_server.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var firstName: String,

    @Column(nullable = false)
    var lastName: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var password: String, // TODO: Hash password before saving

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_permissions",
        joinColumns = [JoinColumn(name = "user_id")]
    )
    @Column(name = "permission")
    @Enumerated(EnumType.STRING)
    val permissions: MutableList<Permission> = mutableListOf()
)
