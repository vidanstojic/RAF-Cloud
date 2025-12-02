package com.server.nvp_server.repository


import com.server.nvp_server.model.User

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long>{
    fun findByEmail(email: String): Optional<User>

}