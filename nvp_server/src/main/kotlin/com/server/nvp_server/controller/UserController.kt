package com.server.nvp_server.controller


import com.server.nvp_server.dto.UserDTO
import com.server.nvp_server.helpers.JwtUtil
import com.server.nvp_server.helpers.RequiresPermission
import com.server.nvp_server.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService, private val passwordEncoder: PasswordEncoder) {


    @PostMapping("/loginuser")
    fun login(@RequestBody req: LoginRequest): LoginResponse {
        println("🔍 Login attempt: ${req.email}")

        val user = userService.findUserByEmail(req.email)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found")
        if (!passwordEncoder.matches(req.password, user.orElseThrow().password)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials")
        }

        val token = JwtUtil.generateToken(user.orElseThrow().email)
        println("✅ Login success: ${user.orElseThrow().email}")
        return LoginResponse(token)
    }

    data class LoginRequest(val email: String, val password: String)
    data class LoginResponse(val token: String)


    @RequiresPermission("READING_USER")
    @GetMapping
    fun getAllUsers(): List<UserDTO> =
        userService.getAllUsers().map { UserMapper.toDTO(it) }

    @RequiresPermission("READING_USER")
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): UserDTO =
        UserMapper.toDTO(userService.getUserById(id)!!)

    @RequiresPermission("CREATING_USER")
    @PostMapping
    fun createUser(@RequestBody dto: UserDTO): UserDTO =
        UserMapper.toDTO(userService.createUser(UserMapper.toEntity(dto)))

    @RequiresPermission("DELETING_USER")
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) {
        userService.deleteUser(id)
    }
    @RequiresPermission("UPDATE_USER")
    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody dto: UserDTO): ResponseEntity<UserDTO> =
        try {
            ResponseEntity.ok(UserMapper.toDTO(userService.updateUser(id, dto)))
        } catch (ex: NoSuchElementException) {
            ResponseEntity.notFound().build()
        } catch (ex: IllegalArgumentException) {
            ResponseEntity.badRequest().build()
        }
}
