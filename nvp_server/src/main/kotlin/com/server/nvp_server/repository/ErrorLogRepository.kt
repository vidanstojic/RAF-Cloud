package com.server.nvp_server.repository

import com.server.nvp_server.model.ErrorLog
import org.springframework.data.jpa.repository.JpaRepository

interface ErrorLogRepository : JpaRepository<ErrorLog, Long> {
    fun findAllByMachineCreatedById(userId: Long): List<ErrorLog>
}