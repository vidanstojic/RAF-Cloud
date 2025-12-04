package com.server.nvp_server.service

import com.server.nvp_server.dto.UserDTO
import com.server.nvp_server.model.Permission
import com.server.nvp_server.model.User
import com.server.nvp_server.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun createUser(user: User): User {
        val hashedPassword = passwordEncoder.encode(user.password)
        val userToSave = user.copy(password = hashedPassword)
        return userRepository.save(userToSave)
    }
    fun findUserByEmail(email :String): Optional<User>{
        return userRepository.findByEmail(email)
    }

    fun getUserById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }


    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }
    fun updateUser(id: Long, dto: UserDTO): User {
        val existing = userRepository.findById(id)
            .orElseThrow { NoSuchElementException("User $id not found") }

        userRepository.findByEmail(dto.email)
            .ifPresent {
                if (it.id != id) throw IllegalArgumentException("E-mail already in use")
            }

        existing.firstName = dto.firstName
        existing.lastName  = dto.lastName
        existing.email     = dto.email
        existing.password  = dto.password


        existing.permissions.clear()
        dto.permissions
            .map { p -> Permission.valueOf(p.uppercase()) }
            .forEach { existing.permissions.add(it) }


        return userRepository.save(existing)
    }
}
