package com.server.nvp_server.controller
import MachineDTO
import com.server.nvp_server.helpers.RequiresPermission
import com.server.nvp_server.model.Machine
import com.server.nvp_server.model.MachineState
import com.server.nvp_server.service.MachineService
import com.server.nvp_server.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
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
import java.security.Principal
import java.util.UUID

@RestController
@RequestMapping("/api/machines")
class MachineController(
    private val machineService: MachineService,
    private val userService: UserService
) {

    @GetMapping
    @RequiresPermission("READING_MACHINE")
    fun getMachines(@RequestParam(required = false) name: String?,
                    @RequestParam(required = false) type: String?,
                    @RequestParam(required = false) state: String?): List<MachineDTO> {
        println(">>> Principal: ${SecurityContextHolder.getContext().authentication}")
        println(">>> Authorities: ${SecurityContextHolder.getContext().authentication?.authorities}")
        return machineService.searchMachines(name, type, state).map { MachineMapper.toDTO(it) };

    }


    @GetMapping("/user/{userId}")
    @RequiresPermission("READING_MACHINE")
    fun getMachinesByUserId(@PathVariable userId: Long?): List<MachineDTO> {
        val effectiveId = userId ?: run {
            val email = SecurityContextHolder.getContext().authentication.name
            userService.findUserByEmail(email).orElseThrow().id
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        }

        return machineService.getMachinesByUser(userService.findUserByEmail(SecurityContextHolder.getContext().authentication.name).orElseThrow())
            .map { MachineMapper.toDTO(it) }
    }

    @PostMapping
    @RequiresPermission("CREATING_MACHINE")
    fun createMachine(@RequestBody dto: MachineDTO): MachineDTO {
        /* uzimamo trenutnog korisnika iz tokena */
        val email = SecurityContextHolder.getContext().authentication.name
        val owner = userService.findUserByEmail(email)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")

        val machine = machineService.createMachine(dto, owner.orElseThrow())
        return MachineMapper.toDTO(machine)
    }
    @GetMapping("/user/{userId}/search")
    @RequiresPermission("READING_MACHINE")
    fun searchUserMachines(
        @PathVariable userId: Long,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) type: String?,
        @RequestParam(required = false) state: String?
    ): List<MachineDTO> =
        machineService.searchUserMachines(userId, name, type, state)
            .map { MachineMapper.toDTO(it) }

    @GetMapping("/{id}")
    @RequiresPermission("READING_MACHINE")
    fun getMachineById(@PathVariable id: Long): MachineDTO =
        MachineMapper.toDTO(machineService.getMachineById(id))

    @PutMapping("/{id}/start")
    @RequiresPermission("STARTING_MACHINE")
    fun startMachine(@PathVariable id: Long) = machineService.startMachine(id)

    @PutMapping("/{id}/stop")
    @RequiresPermission("STOPPING_MACHINE")
    fun stopMachine(@PathVariable id: Long) = machineService.stopMachine(id)

    @PutMapping("/{id}/restart")
    @RequiresPermission("RESTARTING_MACHINE")
    fun restartMachine(@PathVariable id: Long) = machineService.restartMachine(id)

    @DeleteMapping("/{id}")
    @RequiresPermission("DESTROYING_MACHINE")
    fun deleteMachine(@PathVariable id: Long) = machineService.deleteMachine(id)
}
