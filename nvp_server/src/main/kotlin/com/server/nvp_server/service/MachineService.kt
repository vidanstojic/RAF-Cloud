package com.server.nvp_server.service


import com.server.nvp_server.model.ErrorLog
import com.server.nvp_server.model.Machine
import com.server.nvp_server.model.MachineState
import com.server.nvp_server.model.User
import com.server.nvp_server.repository.ErrorLogRepository
import com.server.nvp_server.repository.MachineRepository
import com.server.nvp_server.repository.SchedulerRepository
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
    private val schedulerRepository: SchedulerRepository
) {

    val startedMachines = HashSet<Machine>()
    val restartedMachines = HashSet<Machine>()

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
        machine.active = false
        machineRepository.save(machine)
    }

    fun startMachine(id: Long){
        val machine = getMachineById(id)
        if (restartedMachines.contains(machine) == true || startedMachines.contains(machine) == true){
            val error = ErrorLog(0,"CAN NOT START", machine, "START", LocalDateTime.now())
            errorRepository.save(error)
        }else {
            machine.state = MachineState.OCCUPIED
            GlobalScope.launch {
                delay(10_000)
                machine.state = MachineState.ON
//                println("${m.name} is turned on.")
            }
            startedMachines.add(machine)
        }
    }

    fun restartMachine(id: Long){
        val machine = getMachineById(id)
        startedMachines.remove(machine)
        if (restartedMachines.contains(machine) == true || startedMachines.contains(machine) == true){
            val error = ErrorLog(0,"CAN NOT START", machine, "START", LocalDateTime.now())
            errorRepository.save(error)
        }else {
            restartedMachines.add(machine)
            machine.state = MachineState.OCCUPIED
            val totalDelay = 10_000L
            val halfDelay = totalDelay / 2

            GlobalScope.launch {
                delay(halfDelay)
                machine.state = MachineState.OFF
//                println("${m.name} is shutting down...")

                delay(halfDelay)
                machine.state = MachineState.ON
//                println("${m.name} is restarted.")
            }
            restartedMachines.remove(machine)
            startedMachines.add(machine)
        }
    }

    fun stopMachine(id: Long){
        val machine = getMachineById(id)
        if (restartedMachines.contains(machine) == true || startedMachines.contains(machine) != true){
            val error = ErrorLog(0,"CAN NOT START", machine, "START", LocalDateTime.now())
            errorRepository.save(error)
        }else {
            machine.state = MachineState.OCCUPIED
            GlobalScope.launch {
                delay(10_000)
                machine.state = MachineState.OFF
//                println("${m.name} is turned on.")
            }
            startedMachines.remove(machine)
        }
    }


    @Scheduled(fixedRate = 3000)
    fun checkOperations() {
        val scheduled = schedulerRepository.findAllByScheduledTimeBetween(LocalDateTime.now(), LocalDateTime.now().plusDays(1) )
        for (scheduler in scheduled ){
            if (scheduler.operation.equals("START"))
                startMachine(scheduler.machine.id)
            else if (scheduler.operation.equals("STOP"))
                stopMachine(scheduler.machine.id)
            else if (scheduler.operation.equals("RESTART"))
                restartMachine(scheduler.machine.id)
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
