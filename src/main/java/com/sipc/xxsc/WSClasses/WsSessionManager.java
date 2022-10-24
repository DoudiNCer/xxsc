package com.sipc.xxsc.WSClasses;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WsSessionManager {
    /**
     * 保存连接 session 的地方
     */
    private static ConcurrentHashMap<String, WebSocketSession> SESSION_POOL = new ConcurrentHashMap<>();

    /**
     * 添加 session
     *
     */
    public static void add(String key, WebSocketSession session) {
        // 添加 session
        SESSION_POOL.put(key, session);
    }

    /**
     * 删除 session,会返回删除的 session
     *
     */
    public static WebSocketSession remove(String key) {
        // 删除 session
        return SESSION_POOL.remove(key);
    }

    /**
     * 删除并同步关闭连接
     *
     */
    public static void removeAndClose(String key) {
        WebSocketSession session = remove(key);
        if (session != null) {
            try {
                // 关闭连接
                session.close();
            } catch (IOException e) {
                log.warn(e.getMessage());
                for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                    log.warn("\t" + stackTraceElement.toString());
                }
            }
        }
    }
    /**
     * 删除并同步关闭连接
     *
     */
    public static void removeAndClose(String key, CloseStatus status) {
        WebSocketSession session = remove(key);
        if (session != null) {
            try {
                // 关闭连接
                session.close(status);
            } catch (IOException e) {
                log.warn(e.getMessage());
                for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                    log.warn("\t" + stackTraceElement.toString());
                }
            }
        }
    }
    /**
     * 获得 session
     *
     */
    public static WebSocketSession get(String key) {
        // 获得 session
        return SESSION_POOL.get(key);
    }
}
