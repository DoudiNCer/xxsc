package com.sipc.xxsc.pojo.dto.result.advisory;

import com.sipc.xxsc.pojo.domain.Message;
import com.sipc.xxsc.util.TimeUtils;
import lombok.Data;

@Data
public class MessageResult {
    private boolean fromMe;
    private Integer objectId;
    private String message;
    private Long timestamp;
    private String time;

    public MessageResult(Message message, boolean fromMe) {
        this.fromMe = fromMe;
        this.objectId = message.getTo();
        this.message = message.getMessage();
        this.timestamp = message.getDate();
        this.time = TimeUtils.EasyRead(message.getDate() * 1000);
    }

    public MessageResult() {

    }
}
