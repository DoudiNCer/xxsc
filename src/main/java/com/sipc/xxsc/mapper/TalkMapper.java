package com.sipc.xxsc.mapper;

import com.sipc.xxsc.pojo.domain.Talk;
import com.sipc.xxsc.pojo.po.talk.KeyIndexPo;
import com.sipc.xxsc.pojo.po.talk.MoodIndexPo;

import java.util.List;

public interface TalkMapper {
    List<KeyIndexPo> selectKeyIndex();
    List<MoodIndexPo> selectMoodIndex();
    List<Talk> selectByKey(String key);
}
