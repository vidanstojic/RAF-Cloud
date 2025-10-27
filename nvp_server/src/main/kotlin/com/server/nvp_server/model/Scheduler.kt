package com.server.nvp_server.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "scheduler")
class Scheduler(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var operation: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id", nullable = false)
    var machine: Machine,

    @Column(name = "scheduled_time", nullable = false)
    var scheduledTime: LocalDateTime
)
