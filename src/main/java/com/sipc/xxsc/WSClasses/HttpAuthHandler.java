package com.sipc.xxsc.WSClasses;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.advisory.SendMessageParam;
import com.sipc.xxsc.pojo.dto.result.advisory.MessageResult;
import com.sipc.xxsc.util.WebSocketUtils.AdvisoryUtil;
import com.sipc.xxsc.util.WebSocketUtils.MessageUtil;
import com.sipc.xxsc.util.WebSocketUtils.result.ParseAttributesResult;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;

@Component
@Slf4j
public class HttpAuthHandler extends TextWebSocketHandler {
    /**
     * socket 建立成功事件
     * @param session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        ParseAttributesResult param = AdvisoryUtil.parseAttributes(session.getAttributes());
        if (param == null){
            sendMessage(session, MessageUtil.CommonResult2MsgJson(CommonResult.serverError()));
            try {
                session.close(CloseStatus.SERVER_ERROR);
            } catch (IOException e) {
                log.warn(e.getMessage());
                for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                    log.warn("\t" + stackTraceElement.toString());
                }
            }
            return;
        }
        if (param.getDocIsAi())
            return;
        WsSessionManager.add(AdvisoryUtil.getSessionCode(param, true), session);
        for (MessageResult message : MessageUtil.getStoredMessage(param)) {
            sendMessage(session, MessageUtil.CommonResult2MsgJson(CommonResult.success(message)));
        }
    }

    /**
     * 接收消息事件
     * @param session
     * @param message
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, @NotNull TextMessage message) {
        ParseAttributesResult attributeParam = AdvisoryUtil.parseAttributes(session.getAttributes());
        if (attributeParam == null){
            sendMessage(session, MessageUtil.CommonResult2MsgJson(CommonResult.serverError()));
            try {
                session.close(CloseStatus.SERVER_ERROR);
            } catch (IOException e) {
                log.warn(e.getMessage());
                for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                    log.warn("\t" + stackTraceElement.toString());
                }
            }
            return;
        }
        SendMessageParam param = MessageUtil.msgParamJson2MsgParam(message.getPayload());
        if (param == null) {
            sendMessage(session, MessageUtil.CommonResult2MsgJson(CommonResult.fail("Message格式错误")));
            return;
        }
        WebSocketSession objSession = WsSessionManager.get(AdvisoryUtil.getSessionCode(attributeParam, false));
        boolean isRead = false;
        if (objSession == null)
            log.info("由于" + (attributeParam.getIsDoctor() ? attributeParam.getUserId() : attributeParam.getDoctorId()) + "未上线，"
                    + "用户" + (attributeParam.getIsDoctor() ? attributeParam.getDoctorId() : attributeParam.getUserId()) +  "发送的信息"
                    + param.getMessage() + "未成功发送，记为未读消息。");
        else
            isRead = true;
        String sendMessage = MessageUtil.sendMessage(attributeParam, param, isRead);
        if (isRead)
            sendMessage(objSession, sendMessage);
    }

    /**
     * socket 断开连接时
     * @param session
     * @param status
     */
    @Override
    public void afterConnectionClosed(@NotNull WebSocketSession session, @NotNull CloseStatus status) {
        ParseAttributesResult attributeParam = AdvisoryUtil.parseAttributes(session.getAttributes());
        if (attributeParam == null){
            sendMessage(session, MessageUtil.CommonResult2MsgJson(CommonResult.serverError()));
            try {
                session.close(CloseStatus.SERVER_ERROR);
            } catch (IOException e) {
                log.warn(e.getMessage());
                for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                    log.warn("\t" + stackTraceElement.toString());
                }
            }
            return;
        }
        WsSessionManager.removeAndClose(AdvisoryUtil.getSessionCode(attributeParam,true), status);
    }

    public void sendMessage(WebSocketSession session, String msg) {
        try {
            session.sendMessage(new TextMessage(msg));
        } catch (IOException e) {
            log.warn(e.getMessage());
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                log.warn("\t" + stackTraceElement.toString());
            }
        }
    }
}
