package com.server.nvp_server.service

import com.server.nvp_server.dto.SchedulerDTO
import com.server.nvp_server.mapper.SchedulerMapper
import com.server.nvp_server.model.Machine
import com.server.nvp_server.model.Scheduler
import com.server.nvp_server.repository.MachineRepository
import com.server.nvp_server.repository.SchedulerRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class SchedulerService(
    private val schedulerRepository: SchedulerRepository,
    private val machineRepository: MachineRepository
) {

    fun createSchedule(scheduler: Scheduler, machine: Machine): Scheduler {
        scheduler.machine = machine
        return schedulerRepository.save(scheduler)
    }

    fun getAllSchedules(): List<Scheduler> = schedulerRepository.findAll()

    fun getSchedulesByMachine(machineId: Long): List<Scheduler> =
        schedulerRepository.findAllByMachineId(machineId)

    fun getSchedulesByOperation(operation: String): List<Scheduler> =
        schedulerRepository.findAllByOperation(operation)

    fun getSchedulesBetween(start: LocalDateTime, end: LocalDateTime): List<Scheduler> =
        schedulerRepository.findAllByScheduledTimeBetween(start, end)

    fun deleteSchedule(id: Long) {
        val schedule = schedulerRepository.findById(id)
            .orElseThrow { RuntimeException("Schedule not found") }
        schedulerRepository.delete(schedule)
    }
}
