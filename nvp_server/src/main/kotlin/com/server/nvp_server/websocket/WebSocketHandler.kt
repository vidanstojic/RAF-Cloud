package com.server.nvp_server.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.server.nvp_server.repository.MachineRepository
import com.server.nvp_server.service.MachineService
import com.server.nvp_server.websocket.dto.MachineCommandMessage
import com.server.nvp_server.websocket.dto.MachineStatusMessage
import com.server.nvp_server.websocket.publisher.MachineStatusPublisher
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Component
class WebSocketHandler(
    private val machineService: MachineService,
    private val machineStatusPublisher: MachineStatusPublisher,
    private val machineRepository: MachineRepository
) : TextWebSocketHandler() {

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        try {
            val json = message.payload
            println("Primljeni JSON: $json")

            val command = ObjectMapper().readValue(json, MachineCommandMessage::class.java)

            if(command.machineId == null) return

            val machine = machineRepository.findById(command.machineId).orElse(null)

            if (machine == null) {
                machineStatusPublisher.sendStatusUpdate(MachineStatusMessage(command.machineId, "404"))
                return
            }
        when (command.type) {
            "START" -> {
                    val statusMessage = MachineStatusMessage(
                        machineId = command.machineId,
                        status = "200",
                        progress = 0
                    )
                    machineStatusPublisher.sendStatusUpdate(statusMessage)

                    GlobalScope.launch {
                        try {
                            machineService.startMachine(command.machineId)
                        } catch (e: Exception) {
                            println("Greška prilikom startovanja mašine: ${e.message}")
                        }
                    }
            }
            "STOP" -> {
                val statusMessage = MachineStatusMessage(
                    machineId = command.machineId,
                    status = "201",
                    progress = 100
                )
                machineStatusPublisher.sendStatusUpdate(statusMessage)

                GlobalScope.launch {
                    machineService.stopMachine(command.machineId)
                }
            }
            "RESTART" -> {
                val statusMessage = MachineStatusMessage(
                    machineId = command.machineId,
                    status = "202",
                    progress = 0
                )
                machineStatusPublisher.sendStatusUpdate(statusMessage)

                GlobalScope.launch {
                    machineService.restartMachine(command.machineId)
                }
            }
            else -> println("Unknown command type: ${command.type}")
        }
        } catch (e: Exception) {
            println("!!! KRITIČNA GREŠKA U WS HANDLERU !!!")
            e.printStackTrace()
        }
    }

    override fun afterConnectionEstablished(session: WebSocketSession) {
        println("Client connected: " + session.id)
        machineStatusPublisher.registerSession(session)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        machineStatusPublisher.removeSession(session)
    }
}
