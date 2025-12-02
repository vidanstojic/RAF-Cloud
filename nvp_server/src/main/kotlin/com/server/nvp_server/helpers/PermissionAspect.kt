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

    @Around("@annotation(requiresAny)")  // N O V A
    fun checkAnyPermission(
        joinPoint: ProceedingJoinPoint,
        requiresAny: RequiresAnyPermission
    ): Any? {
        val auth = SecurityContextHolder.getContext().authentication ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
        val user = userService.findUserByEmail(auth.name).orElseThrow { ResponseStatusException(HttpStatus.UNAUTHORIZED) }

        // ADMIN propust
        if (user.permissions.any { it == Permission.ADMIN }) return joinPoint.proceed()

        // ako poseduje BILO KOJU od navedenih – propust
        val required = requiresAny.value
        val userPerms = user.permissions?.map { it.name } ?: emptyList()
        if (required.any { userPerms.contains(it) }) return joinPoint.proceed()

        throw ResponseStatusException(HttpStatus.FORBIDDEN, "No required permission")
    }
}