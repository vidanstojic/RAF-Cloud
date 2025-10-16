package com.server.nvp_server.service

import com.server.nvp_server.model.User
import com.server.nvp_server.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun createUser(firstName: String,lastName:String, email: String, password: String): User {

        val user = User(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = password
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
}
