package common.niuniu.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.server.standard.ServerEndpointExporter

/**
 * WebSocket配置类，用于注册WebSocket的Bean
 */
@Configuration
class WebSocketConfiguration {
    @Bean
    fun serverEndpointExporter(): ServerEndpointExporter {
        return ServerEndpointExporter()
    }
}
