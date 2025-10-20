package com.server.nvp_server


import com.server.nvp_server.model.Permission
import com.server.nvp_server.model.User
import com.server.nvp_server.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component


@Component
class DataLoader(private val userRepository: UserRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {
        //val user = User(firstName = "Pera", lastName = "Peric", email = "pera.peric@example.com", password = "pera123");
        //userRepository.save(user)
        println("Saved users: ${userRepository.findAll()}")
    }
}