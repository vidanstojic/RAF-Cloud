package com.server.nvp_server.controller

import com.server.nvp_server.dto.SchedulerDTO
import com.server.nvp_server.mapper.SchedulerMapper
import com.server.nvp_server.model.Machine
import com.server.nvp_server.model.Scheduler
import com.server.nvp_server.service.MachineService
import com.server.nvp_server.service.SchedulerService
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api")
class SchedulerController(
    private val schedulerService: SchedulerService,
    private val machineService: MachineService
) {

    @GetMapping
    fun getAllSchedules(): List<SchedulerDTO> =
        schedulerService.getAllSchedules().map { SchedulerMapper.toDTO(it) }

    @GetMapping("/machine/{machineId}")
    fun getSchedulesByMachine(@PathVariable machineId: Long): List<SchedulerDTO> =
        schedulerService.getSchedulesByMachine(machineId).map { SchedulerMapper.toDTO(it) }

    @GetMapping("/operation/{operation}")
    fun getSchedulesByOperation(@PathVariable operation: String): List<SchedulerDTO> =
        schedulerService.getSchedulesByOperation(operation).map { SchedulerMapper.toDTO(it) }

    @PostMapping("/machines/{machineId}/schedule")
    fun createSchedule(@PathVariable machineId: Long, @RequestBody dto: SchedulerDTO): SchedulerDTO {
        var machine = machineService.getMachineById(machineId)
        return SchedulerMapper.toDTO(
            schedulerService.createSchedule(SchedulerMapper.toEntity(dto, machine), machine))

    }

    @DeleteMapping("/{id}")
    fun deleteSchedule(@PathVariable id: Long) {
        schedulerService.deleteSchedule(id)
    }
}
