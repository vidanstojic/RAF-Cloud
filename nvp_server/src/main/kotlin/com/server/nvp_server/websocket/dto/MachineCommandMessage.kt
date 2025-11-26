package com.server.nvp_server.websocket.dto

data class MachineCommandMessage(
    val type: String,
    val machineId: Long
)
