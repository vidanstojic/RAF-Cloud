package com.server.nvp_server.service

import com.server.nvp_server.dto.UserDTO
import com.server.nvp_server.model.User
import com.server.nvp_server.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.Optional

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun createUser(user: User): User {

        val user = User(
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            password = user.password
        )

        // Snimamo u bazu
        return userRepository.save(user)
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
        // 1. load attached entity
        val existing = userRepository.findById(id)
            .orElseThrow { NoSuchElementException("User $id not found") }

        // 2. duplicate e-mail guard
        userRepository.findByEmail(dto.email)
            .ifPresent {
                if (it.id != id) throw IllegalArgumentException("E-mail already in use")
            }

        // 3. copy simple fields
        existing.firstName = dto.firstName
        existing.lastName  = dto.lastName
        existing.email     = dto.email
        existing.password  = dto.password          // later hash it

        // 4. replace permissions collection
//        existing.permissions.clear()
//        dto.permissions
//            .map { Permission(name = it) }        // convert String → Permission
//            .forEach { existing.permissions.add(it) }

        // 5. save & return
        return userRepository.save(existing)
    }
}
