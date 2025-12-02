package com.server.nvp_server.helpers

import com.server.nvp_server.model.Permission
import com.server.nvp_server.service.UserService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Aspect
@Component
class PermissionAspect(private val userService: UserService) {

    @Around("@annotation(requiresPermission)")
    fun checkPermission(joinPoint: ProceedingJoinPoint, requiresPermission: RequiresPermission): Any? {
        val auth = SecurityContextHolder.getContext().authentication ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        val email = auth.name
        val userOpt = userService.findUserByEmail(email)
        val user = userOpt.orElseThrow { ResponseStatusException(HttpStatus.UNAUTHORIZED) }

        val permissionNames: List<String> = user.permissions
            ?.map { it.name }
            ?: emptyList()

        if (!permissionNames.contains(requiresPermission.value))
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "No permission: ${requiresPermission.value}")

        return joinPoint.proceed()
    }
}