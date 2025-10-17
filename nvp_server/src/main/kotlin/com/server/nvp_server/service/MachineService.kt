package com.server.nvp_server.service

import com.server.nvp_server.model.Machine
import com.server.nvp_server.model.User
import com.server.nvp_server.repository.MachineRepository
import org.springframework.stereotype.Service

@Service
class MachineService(private val machineRepository: MachineRepository) {

    fun createMachine(machine: Machine, creator: User): Machine {
        machine.createdBy = creator
        machine.state = com.server.nvp_server.model.MachineState.OFF
        return machineRepository.save(machine)
    }

    fun getAllMachines(): List<Machine> = machineRepository.findAll()

    fun getMachinesByUser(user: User): List<Machine> =
        machineRepository.findAllByCreatedById(user.id)

    fun getMachineById(id: Long): Machine =
        machineRepository.findById(id).orElseThrow { RuntimeException("Machine not found") }

    fun updateMachine(machine: Machine): Machine = machineRepository.save(machine)

    fun deleteMachine(id: Long) {
        val machine = getMachineById(id)
        machine.active = false // soft delete
        machineRepository.save(machine)
    }
}
