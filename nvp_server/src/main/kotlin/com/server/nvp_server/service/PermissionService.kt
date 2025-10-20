//package com.server.nvp_server.service
//
//import com.server.nvp_server.model.Permission
//import com.server.nvp_server.repository.PermissionRepository
//import org.springframework.stereotype.Service
//
//@Service
//class PermissionService(private val permissionRepository: PermissionRepository) {
//
//    fun createPermission(permission: Permission): Permission = permissionRepository.save(permission)
//
//    fun getAllPermissions(): List<Permission> = permissionRepository.findAll()
//
//    fun getPermissionById(id: Long): Permission =
//        permissionRepository.findById(id).orElseThrow { RuntimeException("Permission not found") }
//
//    fun getPermissionByName(name: String): Permission =
//        permissionRepository.findByName(name).orElseThrow { RuntimeException("Permission not found") }
//
//    fun deletePermission(id: Long) = permissionRepository.deleteById(id)
//}
