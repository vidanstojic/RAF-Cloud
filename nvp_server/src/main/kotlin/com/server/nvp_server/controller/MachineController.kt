package com.server.nvp_server.controller
import MachineDTO
import com.server.nvp_server.service.MachineService
import com.server.nvp_server.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
@RestController
@RequestMapping("/api/machines")
class MachineController(private val machineService: MachineService) {

//    @GetMapping
//    fun getMachines(@RequestParam(required = false) name: String?,
//                    @RequestParam(required = false) type: String?,
//                    @RequestParam(required = false) state: String?): List<MachineDTO> =
//        machineService.searchMachines(name, type, state).map { MachineMapper.toDTO(it) }
//
//    @PostMapping
//    fun createMachine(@RequestBody dto: MachineDTO): MachineDTO {
//        val owner =
//            return MachineMapper.toDTO(machineService.createMachine(MachineMapper.toEntity(dto, owner)))
//    }
//
//    @PutMapping("/{id}/start")
//    fun startMachine(@PathVariable id: Long) = machineService.startMachine(id)
//
//    @PutMapping("/{id}/stop")
//    fun stopMachine(@PathVariable id: Long) = machineService.stopMachine(id)
//
//    @PutMapping("/{id}/restart")
//    fun restartMachine(@PathVariable id: Long) = machineService.restartMachine(id)
//
//    @DeleteMapping("/{id}")
//    fun deleteMachine(@PathVariable id: Long) = machineService.deleteMachine(id)
}
