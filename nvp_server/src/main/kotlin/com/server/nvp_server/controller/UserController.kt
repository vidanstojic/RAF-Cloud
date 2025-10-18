package com.server.nvp_server.controller


import com.server.nvp_server.dto.UserDTO
import com.server.nvp_server.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

    @GetMapping
    fun getAllUsers(): List<UserDTO> =
        userService.getAllUsers().map { UserMapper.toDTO(it) }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): UserDTO =
        UserMapper.toDTO(userService.getUserById(id)!!)

    /// PROVERITI DA LI OVDEE STAVITI REQUEST BODY ILI MAPPING/NESTO DRUGO
    @PostMapping
    fun createUser(@RequestBody dto: UserDTO): UserDTO =
        UserMapper.toDTO(userService.createUser(UserMapper.toEntity(dto)))

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) {
        userService.deleteUser(id)
    }
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long,
                   @RequestBody dto: UserDTO): UserDTO =
        UserMapper.toDTO(userService.updateUser(id, UserMapper.toEntity(dto)))
}
