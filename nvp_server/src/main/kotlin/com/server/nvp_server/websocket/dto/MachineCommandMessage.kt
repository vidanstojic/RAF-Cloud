package com.server.nvp_server.websocket.dto
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class MachineCommandMessage @JsonCreator constructor(
    @param:JsonProperty("type") val type: String,
    @param:JsonProperty("machineId") val machineId: Long
)
