package com.sipc.xxsc.util.WebSocketUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipc.xxsc.mapper.MessageMapper;
import com.sipc.xxsc.pojo.domain.Message;
import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.advisory.SendMessageParam;
import com.sipc.xxsc.pojo.dto.result.advisory.MessageResult;
import com.sipc.xxsc.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
@Component
@Slf4j
public class MessageUtil {
    @Resource
    private MessageMapper messageMapper;

    private static MessageUtil messageUtil;

    public static String sendMessage(Integer from, SendMessageParam param, Boolean isDoctor, Boolean isRead) {
        Message message = new Message();
        message.setMessage(param.getMessage());
        message.setDate(TimeUtils.getNow());
        message.setFrom(from);
        message.setTo(param.getObjectId());
        message.setIsRead(isRead ? 1 : 0);
        message.setType(isDoctor ? 0 : 1);
        messageUtil.messageMapper.insertMessage(message);
        MessageResult result = new MessageResult(message, isDoctor);
        return CommonResult2MsgJson(CommonResult.success(result));
    }

    @PostConstruct
    public void init() {
        messageUtil = this;
        messageUtil.messageMapper = this.messageMapper;
    }

    public static String CommonResult2MsgJson(CommonResult commonResult) {
        String payloadjson = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            StringWriter stringWriter = new StringWriter();
            JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);
            objectMapper.writeValue(jsonGenerator, commonResult);
            jsonGenerator.close();
            payloadjson = stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payloadjson;
    }

    public static List<MessageResult> getStoredMessage(Integer userId, Integer doctorId, Boolean isDoctor) {
        List<Message> messages = messageUtil.messageMapper.selectMessageByFromAndTo(userId, doctorId);
        List<MessageResult> results = new ArrayList<>();
        for (Message message : messages) {
            results.add(new MessageResult(message, isDoctor));
            if (message.getIsRead() == 0 && (isDoctor ? message.getType() == 1 : message.getType() == 0))
                messageUtil.messageMapper.readMessage(message.getId());
        }
        return results;
    }
    public static SendMessageParam msgParamJson2MsgParam(String paramJson){
        SendMessageParam result = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            result = objectMapper.readValue(paramJson, SendMessageParam.class);
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
        }
        return result;
    }
}
