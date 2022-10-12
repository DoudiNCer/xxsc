package com.sipc.xxsc.pojo.dto.result.advisory;

import com.sipc.xxsc.pojo.domain.Message;
import lombok.Data;

@Data
public class MessageResult {
    private boolean fromMe;
    private Integer objectId;
    private String message;
    private Long timestamp;

    public MessageResult(Message message, Boolean isDoctor) {
        this.fromMe = message.getType() == (isDoctor ? 0 : 1);
        this.objectId = message.getTo();
        this.message = message.getMessage();
        this.timestamp = message.getDate();
    }
}
