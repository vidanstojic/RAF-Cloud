package com.server.nvp_server.model

import jakarta.persistence.*
import java.time.LocalDateTime

enum class MachineState {
    FREE, OCCUPIED, ON, OFF
}

@Entity
@Table(name = "machines")
data class Machine(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var type: String,

    var description: String? = null,

    @Column(nullable = false)
    var uniqueId: String,

    @Enumerated(EnumType.STRING)
    var state: MachineState = MachineState.OFF,

    @ManyToOne
    @JoinColumn(name = "created_by")
    var createdBy: User,

    @Column(nullable = false)
    var active: Boolean = true,

    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
)
