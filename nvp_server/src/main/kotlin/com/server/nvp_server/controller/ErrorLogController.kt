package com.server.nvp_server.controller

import ErrorLogDTO
import ErrorLogMapper
import com.server.nvp_server.helpers.RequiresPermission
import com.server.nvp_server.service.ErrorLogService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/error-logs")
class ErrorLogController(private val service: ErrorLogService) {


    @GetMapping
    @RequiresPermission("READING_ERROR")
    fun getAll(): List<ErrorLogDTO> =
        service.getAllErrorLogs().map(ErrorLogMapper::toDTO)


    @GetMapping("/user/{userId}")
    @RequiresPermission("READING_ERROR")
    fun getByUser(@PathVariable userId: Long): List<ErrorLogDTO> =
        service.getErrorLogsByUser(userId).map(ErrorLogMapper::toDTO)
}