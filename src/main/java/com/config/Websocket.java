package com.config;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


@ServerEndpoint("/send/{group}")
@Component
public class Websocket {

    private static final Logger logger = LoggerFactory.getLogger(Websocket.class);

    public static final String GROUP = "group";

    public static final int maxIdleTimeout = 60 * 60 * 1000;
    public static final String SEND_TOPIC = "sendMessage";

    private static ApplicationContext applicationContext;

    private KafkaTemplate kafkaTemplate;

    private static Map<String, CopyOnWriteArrayList<Session>> webSocketMap = new ConcurrentHashMap<>();

    public static void setApplicationContext(ApplicationContext applicationContext) {
        Websocket.applicationContext = applicationContext;
    }

    /**
     * 连接建立成功调用的方法
     *
     * @param group 组
     */
    @OnOpen
    public void onOpen(@PathParam("group") String group, Session session) {
        if (kafkaTemplate == null) {
            kafkaTemplate = applicationContext.getBean(KafkaTemplate.class);
        }
        CopyOnWriteArrayList<Session> sessionList = webSocketMap.getOrDefault(group, new CopyOnWriteArrayList<>());
        sessionList.add(session);
        webSocketMap.put(group, sessionList);
        session.setMaxIdleTimeout(maxIdleTimeout);
    }


    /**
     * 收到客户端消息后调用的方法 推送给下游组
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String group = jsonObject.getString(GROUP);
        //如果下游有这个组 那就可以触发
        if (webSocketMap.containsKey(group)){
            kafkaTemplate.send(SEND_TOPIC, message);
        }

    }


    /**
     * 连接关闭调用的方法 不移除map数据 因为group总会有的 不然需要每次都判断
     */
    @OnClose
    public void onClose(Session session) throws IOException {
        removeSession(session);
        logger.info("session 关闭");

    }

    private void removeSession(Session session) {
        String group = session.getPathParameters().get(GROUP);
        webSocketMap.get(group).remove(session);
        logger.info("session 移除");
    }


    public void kafkaReceiveMsg(String message) {
        if (message == null) {
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(message);
        String group = jsonObject.getString(GROUP);
        //进行消息发送
        if (webSocketMap.containsKey(group)) {
            webSocketMap.get(group).stream().filter(session -> session.isOpen()).forEach(session -> session.getAsyncRemote().sendText(message));
        }
    }

    /**
     * kafka监听关闭websocket连接
     *
     * @param closeMessage
     */
    public void kafkaCloseWebsocket(String closeMessage) {
        JSONObject jsonObject = JSONObject.parseObject(closeMessage);
        String group = jsonObject.getString(GROUP);
        if (webSocketMap.containsKey(group)) {
            webSocketMap.get(group).forEach(session -> {
                try {
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        webSocketMap.remove(group);
        logger.info("group session 关闭:"+group);
    }


    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) throws IOException {
        logger.error("session 发生错误" + error.getMessage());
        removeSession(session);
    }

}