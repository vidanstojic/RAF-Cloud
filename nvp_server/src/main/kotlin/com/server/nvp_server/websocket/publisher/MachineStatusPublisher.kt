package com.server.nvp_server.websocket.publisher

import com.fasterxml.jackson.databind.ObjectMapper
import com.server.nvp_server.websocket.dto.MachineStatusMessage
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Component
class MachineStatusPublisher(
    private val objectMapper: ObjectMapper
) {

    private val sessions = ConcurrentHashMap.newKeySet<WebSocketSession>()

    fun registerSession(session: WebSocketSession) {
        sessions.add(session)
    }

    fun removeSession(session: WebSocketSession) {
        sessions.remove(session)
    }

    fun sendStatusUpdate(message: MachineStatusMessage) {
        val json = objectMapper.writeValueAsString(message)
        val textMessage = TextMessage(json)

        sessions.forEach { session ->
            if (session.isOpen) {
                session.sendMessage(textMessage)
            }
        }
    }
}
