package com.server.nvp_server


import com.server.nvp_server.model.Permission
import com.server.nvp_server.model.User
import com.server.nvp_server.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component


@Component
class DataLoader(private val userRepository: UserRepository) : CommandLineRunner {
    override fun run(vararg args: String?) {
        if (userRepository.count() == 0L) {
            val admin = User(
                firstName = "Admin",
                lastName = "System",
                email = "admin@raf.rs",
                password = "admin123",
                permissions = mutableListOf(
                    Permission.READING_USER,
                    Permission.CREATING_USER,
                    Permission.UPDATE_USER,
                    Permission.DELETING_USER,
                    Permission.CREATE_MACHINE,
                    Permission.DELETE_MACHINE,
                    Permission.UPDATE_MACHINE,
                    Permission.MACHINE_OPERATION
                )
            )
            userRepository.save(admin)
            println("Admin created with new permissions")
        }
    }
}