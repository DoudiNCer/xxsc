package com.sipc.xxsc.WSChatService;

import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.advisory.SendMessageParam;
import com.sipc.xxsc.pojo.dto.result.advisory.MessageResult;
import com.sipc.xxsc.pojo.dto.resultEnum.ResultEnum;
import com.sipc.xxsc.util.CheckRole.CheckRole;
import com.sipc.xxsc.util.CheckRole.result.JWTCheckResult;
import com.sipc.xxsc.util.WebSocketUtils.MessageUtil;
import com.sipc.xxsc.util.WebSocketUtils.UserDoctorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

@Slf4j
@Component
@ServerEndpoint("/advisory/docChat/{userId}/{token}")
public class AdvisoryChatBWebSocket implements Serializable {
    private static final long serialVersionUID = 130929200302166928L;
    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private Integer userId;
    private Integer doctorId;

    // 连接建立成功调用的方法
    @OnOpen
    public void onOpen(
            Session session, @PathParam("token") String token, @PathParam("userId") Integer userId
    ) {
        CommonResult<JWTCheckResult> check = CheckRole.check(token);
        if (!Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode()))
            sendMessage(MessageUtil.CommonResult2MsgJson(check));
        this.session = session;
        doctorId = check.getData().getUserId();
        this.userId = userId;
        sendMessage(MessageUtil.CommonResult2MsgJson(CommonResult.success("连接成功")));
        for (MessageResult message : MessageUtil.getStoredMessage(userId, doctorId, true)) {
            sendMessage(MessageUtil.CommonResult2MsgJson(CommonResult.success(message)));
        }
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     **/
    @OnMessage
    public void onMessage(String message, Session session){
        SendMessageParam param = MessageUtil.msgParamJson2MsgParam(message);
        if (param == null)
            sendMessage(MessageUtil.CommonResult2MsgJson(CommonResult.fail("message格式错误")));
        else {
            boolean isRead = false;
            AdvisoryChatCWebSocket userSocket = UserDoctorUtil.getUserSocket(userId);
            if (userSocket == null)
                log.info("由于用户" + userId + "未上线，" + "医生" + doctorId +  "发送的信息" + param.getMessage() + "未成功发送，记为未读消息。");
            else
                isRead = true;
            String msgJson = MessageUtil.sendMessage(doctorId, param, true, isRead);
            if (isRead)
                sendMessage(msgJson);
        }
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.doctorId + ",原因:" + error.getMessage());
        sendMessage("用户错误:" + this.doctorId + ",原因:" + error.getMessage());
        error.printStackTrace();
        log.warn(error.getMessage());
    }

    // 连接关闭调用的方法
    @OnClose
    public void onClose() {
        UserDoctorUtil.userExit(userId);
    }
    /**
     * 实现服务
     * 器主动推送
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
