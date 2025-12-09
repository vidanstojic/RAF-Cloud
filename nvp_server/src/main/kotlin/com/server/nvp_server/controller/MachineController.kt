package com.server.nvp_server.controller
import MachineDTO
import MachineMapper
import com.server.nvp_server.helpers.RequiresAnyPermission
import com.server.nvp_server.service.MachineService
import com.server.nvp_server.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/machines")
class MachineController(
    private val machineService: MachineService,
    private val userService: UserService
) {

    @GetMapping
    fun getMachines(@RequestParam(required = false) name: String?,
                    @RequestParam(required = false) type: String?,
                    @RequestParam(required = false) state: String?): List<MachineDTO> {
       return machineService.searchMachines(name, type, state).map { MachineMapper.toDTO(it) };

    }


    @GetMapping("/all")
    @RequiresAnyPermission("ADMIN")
    fun getMachinesAdmin(): List<MachineDTO> {
        val machines = machineService.getAllMachines()
        println(">>> RAW machines from repo: $machines")
        return machines.map { MachineMapper.toDTO(it) }
    }


    @GetMapping("/user/{userId}")
    @RequiresAnyPermission("READING_MACHINE", "ADMIN")
    fun getMachinesByUserId(@PathVariable userId: Long?): List<MachineDTO> {
        val user = userId?.let { userService.getUserById(it) }

        if (user != null)
            return machineService.getMachinesByUser(user).map { MachineMapper.toDTO(it) }
        else
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
    }

    @PostMapping
    @RequiresAnyPermission("CREATING_MACHINE", "ADMIN")
    fun createMachine(@RequestBody dto: MachineDTO): MachineDTO {
        println("Usao u create machine")
        val user = dto.createdBy?.let { userService.getUserById(it) }

        if (user != null)
            return MachineMapper.toDTO(machineService.createMachine(dto,user))
        else
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
    }
    @GetMapping("/user/{userId}/search")
    @RequiresAnyPermission("READING_MACHINE", "ADMIN")
    fun searchUserMachines(
        @PathVariable userId: Long,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) type: String?,
        @RequestParam(required = false) state: String?
    ): List<MachineDTO> =
        machineService.searchUserMachines(userId, name, type, state)
            .map { MachineMapper.toDTO(it) }

    @GetMapping("/{id}")
    @RequiresAnyPermission("READING_MACHINE", "ADMIN")
    fun getMachineById(@PathVariable id: Long): MachineDTO =
        MachineMapper.toDTO(machineService.getMachineById(id))

    @PutMapping("/{id}/start")
    @RequiresAnyPermission("STARTING_MACHINE", "ADMIN")
    fun startMachine(@PathVariable id: Long) = machineService.startMachine(id)

    @PutMapping("/{id}/stop")
    @RequiresAnyPermission("STOPPING_MACHINE", "ADMIN")
    fun stopMachine(@PathVariable id: Long) = machineService.stopMachine(id)

    @PutMapping("/{id}/restart")
    @RequiresAnyPermission("RESTARTING_MACHINE", "ADMIN")
    fun restartMachine(@PathVariable id: Long) = machineService.restartMachine(id)

    @DeleteMapping("/{id}")
    @RequiresAnyPermission("DESTROYING_MACHINE", "ADMIN")
    fun deleteMachine(@PathVariable id: Long) = machineService.deleteMachine(id)
}
