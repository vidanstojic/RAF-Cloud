package com.server.nvp_server.service

import com.server.nvp_server.model.User
import com.server.nvp_server.repository.UserRepository
import org.springframework.stereotype.Service

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


    fun getUserById(id: Long): User? {
        return userRepository.findById(id).orElse(null)
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }


    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }
    fun updateUser(id: Long, incoming: User): User {
        val existing = userRepository.findById(id).orElseThrow()
        existing.firstName  = incoming.firstName
        existing.lastName   = incoming.lastName
        existing.email      = incoming.email          
        existing.password   = incoming.password
        existing.permissions.clear()
        existing.permissions.addAll(incoming.permissions)
        return userRepository.save(existing)
    }
}
