package com.server.nvp_server.repository


import com.server.nvp_server.model.Machine
import org.springframework.data.jpa.repository.JpaRepository

interface MachineRepository : JpaRepository<Machine, Long> {
    fun findAllByCreatedById(userId: Long): List<Machine>
    fun findAllByActiveTrue(): List<Machine>
}