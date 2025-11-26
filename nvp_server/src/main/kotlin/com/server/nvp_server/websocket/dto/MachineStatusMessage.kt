package com.server.nvp_server.websocket.dto

data class MachineStatusMessage(
    val machineId: Long,
    val status: String,
    val progress: Int? = null
)
