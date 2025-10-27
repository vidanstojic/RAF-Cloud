package com.server.nvp_server.repository

import com.server.nvp_server.model.Scheduler
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface SchedulerRepository  : JpaRepository<Scheduler, Long> {

    fun findAllByScheduledTimeBetween(start: LocalDateTime, end: LocalDateTime): List<Scheduler>

    fun findAllByOperation(operation: String): List<Scheduler>

    fun findAllByMachineId(machineId: Long): List<Scheduler>

}