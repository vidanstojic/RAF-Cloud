package com.server.nvp_server.helpers

import com.server.nvp_server.model.Permission
import com.server.nvp_server.service.UserService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

@Aspect
@Component
class PermissionAspect(
    private val userService: UserService
) {

    @Around("@annotation(requiresAny)")
    fun checkAnyPermission(
        joinPoint: ProceedingJoinPoint,
        requiresAny: RequiresAnyPermission
    ): Any? {

        val auth = AuthContext.get()
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated")

        val user = userService.findUserByEmail(auth.email)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found")
        if (user.orElseThrow().permissions.any { it == Permission.ADMIN }) return joinPoint.proceed()
        val required = requiresAny.value
        val userPerms = user.orElseThrow().permissions?.map { it.name } ?: emptyList()
        if (required.any { userPerms.contains(it) }) return joinPoint.proceed()

        throw ResponseStatusException(HttpStatus.FORBIDDEN, "No required permission")
    }
}
