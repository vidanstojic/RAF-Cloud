package com.server.nvp_server.controller
import MachineDTO
import com.server.nvp_server.model.Machine
import com.server.nvp_server.model.MachineState
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
import java.security.Principal
import java.util.UUID

@RestController
@RequestMapping("/api/machines")
class MachineController(
    private val machineService: MachineService,
    private val userService: UserService
) {

    @GetMapping
    fun getMachines(@RequestParam(required = false) name: String?,
                    @RequestParam(required = false) type: String?,
                    @RequestParam(required = false) state: String?): List<MachineDTO> =
        machineService.searchMachines(name, type, state).map { MachineMapper.toDTO(it) }

    @GetMapping("/user/{userId}")
    fun getMachinesByUserId(@PathVariable userId: Long): List<MachineDTO> =
        machineService.getMachinesByUser(userService.getUserById(userId)!!)
            .map { MachineMapper.toDTO(it) }


    @PostMapping
    fun createMachine(@RequestBody dto: MachineDTO): MachineDTO {
        val owner = userService.getUserById(dto.createdBy!!)   // ← use the id you receive
        return MachineMapper.toDTO(
            machineService.createMachine(MachineMapper.toEntity(dto, owner!!), owner)
        )
    }
    @GetMapping("/user/{userId}/search")
    fun searchUserMachines(
        @PathVariable userId: Long,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) type: String?,
        @RequestParam(required = false) state: String?
    ): List<MachineDTO> =
        machineService.searchUserMachines(userId, name, type, state)
            .map { MachineMapper.toDTO(it) }

//    @PutMapping("/{id}/start")
//    fun startMachine(@PathVariable id: Long) = machineService.startMachine(id)
//
//    @PutMapping("/{id}/stop")
//    fun stopMachine(@PathVariable id: Long) = machineService.stopMachine(id)
//
//    @PutMapping("/{id}/restart")
//    fun restartMachine(@PathVariable id: Long) = machineService.restartMachine(id)

    @DeleteMapping("/{id}")
    fun deleteMachine(@PathVariable id: Long) = machineService.deleteMachine(id)
}
