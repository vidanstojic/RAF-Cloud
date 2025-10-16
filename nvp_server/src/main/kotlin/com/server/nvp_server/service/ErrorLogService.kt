package com.server.nvp_server.service

import com.server.nvp_server.model.ErrorLog
import com.server.nvp_server.repository.ErrorLogRepository
import org.springframework.stereotype.Service

@Service
class ErrorLogService(private val errorLogRepository: ErrorLogRepository) {

    fun createErrorLog(errorLog: ErrorLog): ErrorLog = errorLogRepository.save(errorLog)

    fun getAllErrorLogs(): List<ErrorLog> = errorLogRepository.findAll()

    fun getErrorLogsByUser(userId: Long): List<ErrorLog> =
        errorLogRepository.findAllByMachineCreatedById(userId)
}
