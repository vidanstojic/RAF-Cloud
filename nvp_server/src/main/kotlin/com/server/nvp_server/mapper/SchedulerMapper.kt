package com.server.nvp_server.mapper

import com.server.nvp_server.dto.SchedulerDTO
import com.server.nvp_server.model.Machine
import com.server.nvp_server.model.MachineState
import com.server.nvp_server.model.Scheduler
import com.server.nvp_server.model.User
import java.util.*

object SchedulerMapper {

    fun toDTO(scheduler: Scheduler) = SchedulerDTO(
        id = scheduler.id,
        operation = scheduler.operation,
        machineId = scheduler.machine.id,
        scheduledTime = scheduler.scheduledTime,
    )

    fun toEntity(dto: SchedulerDTO, machine: Machine) = Scheduler(
        id = dto.id ?: null,
        operation = dto.operation?: "Null",
        machine = machine,
        scheduledTime = dto.scheduledTime
    )
}