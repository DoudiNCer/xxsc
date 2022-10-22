package com.sipc.xxsc.util.talkAi;

import com.sipc.xxsc.mapper.TalkMapper;
import com.sipc.xxsc.pojo.domain.Talk;
import com.sipc.xxsc.pojo.po.talk.KeyIndexPo;
import com.sipc.xxsc.pojo.po.talk.MoodIndexPo;
import com.sipc.xxsc.util.redis.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
public class TalkAiUtil {
    @Resource
    private TalkMapper talkMapper;
    @Resource
    RedisUtil redisUtil;
    private static TalkAiUtil talkAiUtil;
    private static final String ISENABLEKEY = "AiDbRdsIsInited";
    private static final String KEYP = "AiKey";
    private static final String KEYE = "KeyAi";
    private static final String MOODP = "AiMood";
    private static final String MOODE = "MoodAi";
    private static final String TALKP = "talkAi";
    private static final String TALKE = "AiTalk";
    private static final String MOODINDEX = "AiMoodIndex";
    private static final String KEYINDEX = "AiKeyIndex";
    @PostConstruct
    public void init() {
        talkAiUtil = this;
        talkAiUtil.talkMapper = this.talkMapper;
        talkAiUtil.redisUtil = this.redisUtil;
        if (talkAiUtil.redisUtil.exists(ISENABLEKEY))
            return;
        List<KeyIndexPo> keyIndexPos = talkAiUtil.talkMapper.selectKeyIndex();
        List<String> keyIndexList = new ArrayList<>();
        for (KeyIndexPo keyIndex : keyIndexPos) {
            keyIndexList.add(keyIndex.getKey());
            talkAiUtil.redisUtil.sadd(getMoodKeyKey(keyIndex.getKey()), keyIndex.getIds());
            for (Talk talk : talkAiUtil.talkMapper.selectByKey(keyIndex.getKey())) {
                talkAiUtil.redisUtil.set(getTalkObjKey(talk.getId()), talk);
            }
        }
        talkAiUtil.redisUtil.set(KEYINDEX, keyIndexList);
        List<MoodIndexPo> moodIndexPos = talkAiUtil.talkMapper.selectMoodIndex();
        for (MoodIndexPo moodIndexPo : moodIndexPos) {
            talkAiUtil.redisUtil.sadd(getMoodKey(moodIndexPo.getMood()), moodIndexPo.getIds());
        }
        talkAiUtil.redisUtil.set(ISENABLEKEY, 1);
    }
    private static String getTalkObjKey(Integer id){
        return TALKP + id + TALKE;
    }
    private static String getMoodKey(Integer mood){
        return MOODP + mood + MOODE;
    }
    private static String getMoodKeyKey(String moodKey){
        return KEYP + moodKey + KEYE;
    }
    public static List<String> talk(String str){
        List<String> results = new ArrayList<>();
        results.add("猫猫姐姐好美！！");
        results.add("我要做猫猫姐姐的狗!!");
        return results;
    }
}
