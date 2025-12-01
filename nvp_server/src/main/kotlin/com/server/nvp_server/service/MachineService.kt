package com.server.nvp_server.service


import MachineDTO
import com.server.nvp_server.model.ErrorLog
import com.server.nvp_server.model.Machine
import com.server.nvp_server.model.MachineState
import com.server.nvp_server.model.User
import com.server.nvp_server.repository.ErrorLogRepository
import com.server.nvp_server.repository.MachineRepository
import com.server.nvp_server.repository.SchedulerRepository
import com.server.nvp_server.websocket.dto.MachineStatusMessage
import com.server.nvp_server.websocket.publisher.MachineStatusPublisher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MachineService(
    private val machineRepository: MachineRepository,
    private val errorRepository: ErrorLogRepository,
    private val schedulerRepository: SchedulerRepository,
    private val machineStatusPublisher: MachineStatusPublisher
) {
    fun createMachine(machine: Machine, creator: User): Machine {
        machine.createdBy = creator
        machine.state = MachineState.OFF
        return machineRepository.save(machine)
    }

    fun createMachine(dto: MachineDTO, owner: User): Machine {
        val machine = Machine(
            name = dto.name,
            type = dto.type,
            description = dto.description,
            createdBy = owner,
            active = true,
            state = MachineState.OFF,
            uniqueId = dto.uniqueId.toString()
        )
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
        machine.active = false
        machineRepository.save(machine)
    }

    fun startMachine(id: Long){

        val machine = getMachineById(id)

        if (machine.state == MachineState.ON){
            errorRepository.save(ErrorLog(0,"Can not turn on machine that is already turned on",machine,"Starting"))
            machineStatusPublisher.sendStatusUpdate(MachineStatusMessage(machine.id,"404",0))
            return
        }else if (machine.state == MachineState.OCCUPIED){
            errorRepository.save(ErrorLog(0,"Can not turn on machine that is occupied",machine,"Starting"))
            machineStatusPublisher.sendStatusUpdate(MachineStatusMessage(machine.id,"404",0))
            return
        }

        GlobalScope.launch {
            delay(10_000)

            machine.state = MachineState.ON
            machineRepository.save(machine)

            val runningMsg = MachineStatusMessage(
                machineId = machine.id,
                status = "RUNNING",
                progress = 100
            )
            machineStatusPublisher.sendStatusUpdate(runningMsg)
            println("POSLATO: RUNNING status nakon 10 sekundi za mašinu ${machine.name}")
        }
    }

    fun stopMachine(id: Long) {
        val machine = machineRepository.findById(id).orElseThrow {
            RuntimeException("Mašina ID $id nije pronađena")
        }

        if (machine.state == MachineState.OFF){
            errorRepository.save(ErrorLog(0,"Can not turn off machine that is already turned off",machine,"Stopping"))
            machineStatusPublisher.sendStatusUpdate(MachineStatusMessage(machine.id,"404",0))
            return
        }else if (machine.state == MachineState.OCCUPIED){
            errorRepository.save(ErrorLog(0,"Can not turn off machine that is occupied",machine,"Stopping"))
            machineStatusPublisher.sendStatusUpdate(MachineStatusMessage(machine.id,"404",0))
            return
        }

        machine.state = MachineState.OCCUPIED
        machineRepository.save(machine)

        GlobalScope.launch {
            println("Zaustavljanje mašine ${machine.name}. Čekanje 10 sekundi...")
            delay(10_000)

            machine.state = MachineState.OFF
            machineRepository.save(machine)

            val statusMessage = MachineStatusMessage(
                machineId = id,
                status = "STOPPED",
                progress = 100
            )
            machineStatusPublisher.sendStatusUpdate(statusMessage)
            println("POSLATO: STOPPED status nakon 10 sekundi za mašinu ${machine.name}")
        }
    }

    fun restartMachine(id: Long) {
        val machine = machineRepository.findById(id).orElseThrow {
            RuntimeException("Mašina ID $id nije pronađena")
        }

        if (machine.state == MachineState.OFF){
            errorRepository.save(ErrorLog(0,"Can not restart machine that is turned off",machine,"Restarting"))
            machineStatusPublisher.sendStatusUpdate(MachineStatusMessage(machine.id,"404",0))
            return
        }else if (machine.state == MachineState.OCCUPIED){
            errorRepository.save(ErrorLog(0,"Can not turn on machine that is occupied",machine,"Restarting"))
            machineStatusPublisher.sendStatusUpdate(MachineStatusMessage(machine.id,"404",0))
            return
        }

        machine.state = MachineState.OCCUPIED
        val totalDelay = 10_000L
        val halfDelay = totalDelay / 2

        GlobalScope.launch {
            println("Restartovanje mašine ${machine.name}. Čekanje 10 sekundi...")
            delay(halfDelay)
            machine.state = MachineState.OFF
            delay(halfDelay)
            machine.state = MachineState.ON
            val statusMessage = MachineStatusMessage(
                machineId = id,
                status = "RUNNING",
                progress = 100
            )
            machineStatusPublisher.sendStatusUpdate(statusMessage)
            println("POSLATO: RUNNING status nakon 10 sekundi za mašinu ${machine.name}")
        }
    }

    @Scheduled(fixedRate = 3000)
    fun checkOperations() {
        val scheduled = schedulerRepository.findAllByScheduledTimeBetween(LocalDateTime.now().minusMinutes(1), LocalDateTime.now())

        for (scheduler in scheduled) {
            val statusMessage = MachineStatusMessage(
                machineId = scheduler.machine.id,
                status = "SCHEDULED_${scheduler.operation}",
                progress = 0
            )
            println(statusMessage)
            machineStatusPublisher.sendStatusUpdate(statusMessage)

            if (scheduler.operation.equals("start"))
                startMachine(scheduler.machine.id)
            else if (scheduler.operation.equals("stop"))
                stopMachine(scheduler.machine.id)
            else if (scheduler.operation.equals("restart"))
                restartMachine(scheduler.machine.id)

            schedulerRepository.delete(scheduler)
        }
    }

    fun searchMachines(name: String?, type: String?, state: String?): List<Machine> {
        val base = machineRepository.findAll()

        return base
            .asSequence()
            .filter { m ->
                name.isNullOrBlank() || m.name.contains(name, ignoreCase = true)
            }
            .filter { m ->
                type.isNullOrBlank() || m.type.equals(type, ignoreCase = true)
            }
            .filter { m ->
                state.isNullOrBlank() || m.state.name.equals(state, ignoreCase = true)
            }
            .toList()
    }

    fun searchUserMachines(
        userId: Long,
        name: String?,
        type: String?,
        state: String?
    ): List<Machine> {
        val base = machineRepository.findAllByCreatedById(userId)
        return base
            .filter { name.isNullOrBlank() || it.name.contains(name, ignoreCase = true) }
            .filter { type.isNullOrBlank() || it.type.equals(type, ignoreCase = true) }
            .filter { state.isNullOrBlank() || it.state.name.equals(state, ignoreCase = true) }
    }
}
