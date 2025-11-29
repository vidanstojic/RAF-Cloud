package com.server.nvp_server.websocket

import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val machineWebSocketHandler: MachineWebSocketHandler,
//    private val authInterceptor: WebSocketAuthInterceptor
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(machineWebSocketHandler, "/ws/machines")
//            .addInterceptors(authInterceptor)
            .setAllowedOrigins("*")
    }
}
