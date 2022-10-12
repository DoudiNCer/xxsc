package com.sipc.xxsc.mapper;

import com.sipc.xxsc.pojo.domain.Message;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MessageMapper {
    int insertMessage(Message message);
    List<Message> selectMessageByFromAndTo(@Param("u0") Integer userId0, @Param("u1") Integer userId1);
    int readMessage(Integer messageId);
    int readAllMessage(@Param("from") Integer from, @Param("to") Integer to);
}
