package com.sipc.xxsc.mapper;

import com.sipc.xxsc.pojo.domain.Comment;
import com.sipc.xxsc.pojo.po.CommentPo;

import java.util.List;

public interface CommentMapper {
    Integer insert(Comment comment);

    List<CommentPo> selectByStoryId(Integer page, Integer storyId);
}
