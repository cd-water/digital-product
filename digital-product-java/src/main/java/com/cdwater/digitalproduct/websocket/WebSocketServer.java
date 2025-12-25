package com.cdwater.digitalproduct.websocket;

import com.cdwater.digitalproduct.common.enums.ReturnMsg;
import com.cdwater.digitalproduct.common.exception.BusinessException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@ServerEndpoint("/ws/{shopId}")
public class WebSocketServer {

    //存放会话对象
    private static final Map<String, Session> sessionMap = new HashMap<>();

    /**
     * 建立连接回调
     *
     * @param session 会话对象
     * @param shopId  店铺id
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("shopId") String shopId) {
        sessionMap.put(shopId, session);
        log.info("店铺客户端：[{}]建立连接;目前在线连接数——{}", shopId, sessionMap.size());
    }

    /**
     * 接收客户端消息回调
     *
     * @param message 消息：客户端->服务器
     * @param shopId  店铺id
     */
    @OnMessage
    public void onMessage(String message, @PathParam("shopId") String shopId) {
        log.info("接收客户端消息：店铺[{}]——[{}]", shopId, message);
    }

    /**
     * 断开连接回调
     *
     * @param shopId 店铺id
     */
    @OnClose
    public void onClose(@PathParam("shopId") String shopId) {
        sessionMap.remove(shopId);
        log.info("店铺客户端：[{}]断开连接;目前在线连接数——{}", shopId, sessionMap.size());
    }

    /**
     * 服务器发送消息给指定客户端
     *
     * @param message 消息：服务器->客户端
     * @param shopId  店铺id
     */
    public void sendToClient(String message, String shopId) {
        Session session = sessionMap.get(shopId);
        if (session != null) {
            try {
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                throw new BusinessException(ReturnMsg.SYSTEM_ERROR);
            }
        }
    }
}
