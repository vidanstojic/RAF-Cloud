package com.server.nvp_server.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "error_logs")
data class ErrorLog(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var message: String,

    @ManyToOne
    @JoinColumn(name = "machine_id")
    var machine: Machine,

    @Column(nullable = false)
    var operation: String,

    @Column(nullable = false)
    var timestamp: LocalDateTime = LocalDateTime.now()
)
