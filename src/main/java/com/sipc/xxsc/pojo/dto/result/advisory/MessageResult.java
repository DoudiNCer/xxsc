package com.sipc.xxsc.pojo.dto.result.advisory;

import com.sipc.xxsc.pojo.domain.Message;
import com.sipc.xxsc.util.WebSocketUtils.result.ParseAttributesResult;
import lombok.Data;

@Data
public class MessageResult {
    private boolean fromMe;
    private Integer objectId;
    private String message;
    private Long timestamp;

    public MessageResult(Message message, boolean fromMe) {
        this.fromMe = fromMe;
        this.objectId = message.getTo();
        this.message = message.getMessage();
        this.timestamp = message.getDate();
    }

    public MessageResult() {

    }
}
