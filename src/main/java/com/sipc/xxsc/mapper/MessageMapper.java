package com.sipc.xxsc.mapper;

import com.sipc.xxsc.pojo.domain.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper {
    int insertMessage(Message message);
    List<Message> selectMessageByFromAndTo(@Param("from") Integer from, @Param("to") Integer to);
    int readMessage(Integer messageId);
    int readAllMessage(@Param("from") Integer from, @Param("to") Integer to);
}
