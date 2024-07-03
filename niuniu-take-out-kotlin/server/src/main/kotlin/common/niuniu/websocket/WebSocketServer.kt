package common.niuniu.websocket

import jakarta.websocket.OnClose
import jakarta.websocket.OnMessage
import jakarta.websocket.OnOpen
import jakarta.websocket.Session
import jakarta.websocket.server.PathParam
import jakarta.websocket.server.ServerEndpoint
import org.springframework.stereotype.Component

/**
 * WebSocket服务端组件，用于和客户端通信
 */
@Component
@ServerEndpoint("/ws/{sid}")
class WebSocketServer {
    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    fun onOpen(session: Session?, @PathParam("sid") sid: String) {
        println("客户端：" + sid + "建立连接")
        sessionMap[sid] = session
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    fun onMessage(message: String, @PathParam("sid") sid: String) {
        println("收到来自客户端：" + sid + "的信息:" + message)
    }

    /**
     * 连接关闭调用的方法
     *
     * @param sid
     */
    @OnClose
    fun onClose(@PathParam("sid") sid: String) {
        println("连接断开:$sid")
        sessionMap.remove(sid)
    }

    /**
     * 群发
     *
     * @param message
     */
    fun sendToAllClient(message: String?) {
        val sessions: Collection<Session?> = sessionMap.values
        for (session in sessions) {
            try {
                // 服务器向客户端发送消息
                session!!.basicRemote.sendText(message)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        // 存放会话对象
        private val sessionMap: MutableMap<String?, Session?> = mutableMapOf()
    }
}