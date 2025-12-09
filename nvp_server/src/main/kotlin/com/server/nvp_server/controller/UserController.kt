package com.server.nvp_server.controller


import com.server.nvp_server.dto.UserDTO
import com.server.nvp_server.helpers.JwtUtil
import com.server.nvp_server.helpers.RequiresAnyPermission
import com.server.nvp_server.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.mindrot.jbcrypt.BCrypt
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
class UserController(
    private val userService: UserService,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/loginuser")
    fun login(@RequestBody req: LoginRequest): LoginResponse {

        val user = userService.findUserByEmail(req.email)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found")

        val isValidPassword = BCrypt.checkpw(req.password, user.orElseThrow().password)
        if (!isValidPassword) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials")
        }

        val perms = user.orElseThrow().permissions?.map { it.name } ?: emptyList()

        val token = jwtUtil.generateToken(user.orElseThrow().email, perms, user.orElseThrow().id)

        return LoginResponse(token, perms)
    }
    data class LoginRequest(val email: String, val password: String)
    data class LoginResponse(val token: String,val permissions: List<String>)

    @RequiresAnyPermission("READING_USER", "ADMIN")
    @GetMapping
    fun getAllUsers(request: HttpServletRequest): List<UserDTO> {
        return userService.getAllUsers().map { UserMapper.toDTO(it) }
    }

    @RequiresAnyPermission("READING_USER", "ADMIN")
    @GetMapping("/{email}")
    fun getUserByEmail(@PathVariable email: String): UserDTO =
        UserMapper.toDTO(userService.findUserByEmail(email).orElseThrow())


    @RequiresAnyPermission("READING_USER", "ADMIN")
    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): UserDTO =
        UserMapper.toDTO(userService.getUserById(id)!!)

    @RequiresAnyPermission("CREATING_USER", "ADMIN")
    @PostMapping
    fun createUser(@RequestBody dto: UserDTO): UserDTO =
        UserMapper.toDTO(userService.createUser(UserMapper.toEntity(dto)))

    @RequiresAnyPermission("DELETING_USER", "ADMIN")
    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) {
        userService.deleteUser(id)
    }

    @RequiresAnyPermission("UPDATE_USER", "ADMIN")
    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody dto: UserDTO,
        request: HttpServletRequest
    ): ResponseEntity<Any> = try {

        val updated = userService.updateUser(id, dto)
        val perms = updated.permissions?.map { it.name } ?: emptyList()


        val body: MutableMap<String, Any> = mutableMapOf(
            "user" to UserMapper.toDTO(updated)
        )


        ResponseEntity.ok(body)
    } catch (ex: NoSuchElementException) {
        ResponseEntity.notFound().build()
    } catch (ex: IllegalArgumentException) {
        ResponseEntity.badRequest().build()
    }
}
