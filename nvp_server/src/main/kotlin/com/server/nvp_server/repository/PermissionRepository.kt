//package com.server.nvp_server.repository
//
//import com.server.nvp_server.model.Permission
//import org.springframework.data.jpa.repository.JpaRepository
//import java.util.Optional
//
//interface PermissionRepository : JpaRepository<Permission, Long> {
//    fun findByName(name: String): Optional<Permission>
//}