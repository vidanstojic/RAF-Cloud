package com.server.nvp_server.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.server.nvp_server.service.MachineService
import com.server.nvp_server.websocket.dto.MachineCommandMessage
import com.server.nvp_server.websocket.publisher.MachineStatusPublisher
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class MachineWebSocketHandler(
    private val machineService: MachineService,
    private val machineStatusPublisher: MachineStatusPublisher
) : TextWebSocketHandler() {

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val json = message.payload
        val command = ObjectMapper().readValue(json, MachineCommandMessage::class.java)

        when (command.type) {
            "START" -> {
                // Poziv logike na servisu
                machineService.startMachine(command.machineId)

//                // Odmah pošalji ažurirani status, npr. da je POKRENUT (može biti i STARTING)
//                val statusMessage = MachineStatusMessage(
//                    machineId = command.machineId,
//                    status = "RUNNING",
//                    progress = 0
//                )
//                machineStatusPublisher.sendStatusUpdate(statusMessage)//VEROVATNO NE TREBA
            }
            "STOP" -> {
                machineService.stopMachine(command.machineId)
//
//                // Pošalji status STOPIRAN
//                val statusMessage = MachineStatusMessage(
//                    machineId = command.machineId,
//                    status = "STOPPED",
//                    progress = 100
//                )
//                machineStatusPublisher.sendStatusUpdate(statusMessage)
            }
            "RESTART" -> {
                machineService.restartMachine(command.machineId)
//
//                // Pošalji status RESTARTING
//                val statusMessage = MachineStatusMessage(
//                    machineId = command.machineId,
//                    status = "RESTARTING",
//                    progress = 0
//                )
//                machineStatusPublisher.sendStatusUpdate(statusMessage)
            }
            else -> println("Unknown command type: ${command.type}")
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
