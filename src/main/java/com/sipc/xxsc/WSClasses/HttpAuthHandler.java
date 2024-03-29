package com.sipc.xxsc.WSClasses;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.advisory.SendMessageParam;
import com.sipc.xxsc.pojo.dto.result.advisory.MessageResult;
import com.sipc.xxsc.util.TimeUtils;
import com.sipc.xxsc.util.WebSocketUtils.AdvisoryUtil;
import com.sipc.xxsc.util.WebSocketUtils.MessageUtil;
import com.sipc.xxsc.util.WebSocketUtils.result.ParseAttributesResult;
import com.sipc.xxsc.util.talkAi.TalkAiUtil;
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
            log.info(attributeParam.toString() + "发送的message格式错误，内容为：\n\t" + message.getPayload());
            sendMessage(session, MessageUtil.CommonResult2MsgJson(CommonResult.fail("Message格式错误")));
            return;
        }
        if (attributeParam.getDocIsAi()){
            for (String s : TalkAiUtil.talk(param.getMessage(), attributeParam)) {
                MessageResult result = new MessageResult();
                result.setMessage(s);
                result.setFromMe(false);
                result.setObjectId(0);
                result.setTimestamp(TimeUtils.getNow());
                result.setTime(TimeUtils.EasyRead(TimeUtils.getNow() * 1000));
                sendMessage(session,
                        MessageUtil.CommonResult2MsgJson(
                                CommonResult.success(result)));
            }
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
        if (attributeParam.getDocIsAi()) {
            try {
                session.close();
            } catch (IOException e) {
                log.warn(e.getMessage());
                for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                    log.warn("\t" + stackTraceElement.toString());
                }
            }
        } else
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
