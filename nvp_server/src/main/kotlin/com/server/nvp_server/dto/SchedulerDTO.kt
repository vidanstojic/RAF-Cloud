package com.server.nvp_server.dto

import java.time.LocalDateTime

data class SchedulerDTO(
    var id: Long? = null,
    var operation: String? = null,
    var machineId: Long,
    var scheduledTime: LocalDateTime
)
