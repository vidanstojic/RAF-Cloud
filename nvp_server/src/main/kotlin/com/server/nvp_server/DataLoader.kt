package com.server.nvp_server


import com.server.nvp_server.model.Permission
import com.server.nvp_server.model.User
import com.server.nvp_server.repository.UserRepository
import com.server.nvp_server.service.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component


@Component
class DataLoader(private val userRepository: UserRepository,
                 private val userService: UserService) : CommandLineRunner {
    override fun run(vararg args: String?) {
        if (userRepository.count() == 0L) {
            val admin = User(
                firstName = "Adminovac",
                lastName = "Ne",
                email = "admin@raf.rs",
                password = "admin123",
                permissions = mutableListOf(
                    Permission.ADMIN
                )
            )
            userService.createUser(admin)
            println("Admin created with new permissions")
        }
    }
}