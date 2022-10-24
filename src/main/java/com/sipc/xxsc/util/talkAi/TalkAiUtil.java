package com.sipc.xxsc.util.talkAi;

import com.sipc.xxsc.mapper.HollowMapper;
import com.sipc.xxsc.mapper.MoodMapper;
import com.sipc.xxsc.mapper.TalkMapper;
import com.sipc.xxsc.mapper.TodoMapper;
import com.sipc.xxsc.pojo.domain.Hollow;
import com.sipc.xxsc.pojo.domain.Mood;
import com.sipc.xxsc.pojo.domain.Talk;
import com.sipc.xxsc.pojo.domain.Todo;
import com.sipc.xxsc.pojo.po.talk.KeyIndexPo;
import com.sipc.xxsc.pojo.po.talk.MoodIndexPo;
import com.sipc.xxsc.util.RandomUtil;
import com.sipc.xxsc.util.TimeUtils;
import com.sipc.xxsc.util.WebSocketUtils.result.ParseAttributesResult;
import com.sipc.xxsc.util.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class TalkAiUtil {
    @Resource
    TodoMapper todoMapper;
    @Resource
    MoodMapper moodMapper;
    @Resource
    HollowMapper hollowMapper;
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
    private static final String KEYINDEX = "AiKeyIndex";
    @PostConstruct
    public void init() {
        talkAiUtil = this;
        talkAiUtil.talkMapper = this.talkMapper;
        talkAiUtil.redisUtil = this.redisUtil;
        talkAiUtil.todoMapper = this.todoMapper;
        talkAiUtil.moodMapper = this.moodMapper;
        talkAiUtil.hollowMapper = this.hollowMapper;
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
        talkAiUtil.redisUtil.setListByJson(KEYINDEX, keyIndexList);
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
    private static List<String> getKeyIndexList(){
        return talkAiUtil.redisUtil.getListByJson(KEYINDEX);
    }
    private static List getRandomKeys(String key, Integer num){
        return talkAiUtil.redisUtil.srandmember(getMoodKeyKey(key), num);
    }
    private static Talk getTalk(Integer id){
        Object o = talkAiUtil.redisUtil.get(getTalkObjKey(id));
        Talk result = null;
        try {
            result = (Talk) o;
        } catch (Exception e){
            log.warn(e.getMessage());
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                log.warn("\t" + stackTraceElement.toString());
            }
        }
        return result;
    }
    public static List<String> talk(String str, ParseAttributesResult param){
        List<String> results = new ArrayList<>();
        for (String key : getKeyIndexList()) {
            if (str.contains(key)){
                List<String> moodResult = new ArrayList<>();
                for (Object keyIdo : getRandomKeys(key, 1)) {
                    Integer keyId = null;
                    try {
                        keyId = (Integer) keyIdo;
                    } catch (Exception e){
                        log.warn(e.getMessage());
                        for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                            log.warn("\t" + stackTraceElement.toString());
                        }
                    }
                    if (keyId == null)
                        continue;
                    Talk talk = getTalk(keyId);
                    if (talk == null)
                        continue;
                    moodResult.add(talk.getValue());
                    switch (talk.getType()){
                        case 1:
                            List<Todo> todos = talkAiUtil.todoMapper.selectTodayTodosByUserId(param.getUserId());
                            Todo todo = todos.get(RandomUtil.getRandomInt(0, todos.size() - 1));
                            if (todo.getFinish() == 0)
                                moodResult.add("不要忘了" + todo.getTodo() + "哦");
                            else
                                moodResult.add("你今天完成了Todo“" + todo.getTodo() + "”，真棒！");
                            break;
                        case 2:
                            List<Mood> moods = talkAiUtil.moodMapper.selectByUserId(param.getUserId());
                            Mood mood = moods.get(0);
                            if (mood == null || mood.getDate() < TimeUtils.getDayTime())
                                continue;
                            if (Objects.equals(mood.getMood(), talk.getMood())){
                                switch (mood.getMood()){
                                    case 0:
                                        moodResult.add("看来你今天很开心嘛，继续保持好心情！");
                                        break;
                                    case 1:
                                        break;
                                    case 2:
                                        moodResult.add("看来你今天心情不好，尝试做一些自己喜欢的事叭，喵呜！");
                                        break;
                                }
                            } else {
                                if (mood.getMood() > talk.getMood()){
                                    moodResult.add("不经历风雨怎能见彩虹，你经历了这么多，以后的生活一定会变好的！");
                                } else {
                                    moodResult.add("心情好起来了呢，喵喵猫！");
                                }
                            }
                            break;
                        case 3:
                            Hollow hollow = talkAiUtil.hollowMapper.selectById(
                                    RandomUtil.getRandomInt(0, talkAiUtil.hollowMapper.selectCount())
                            );
                            moodResult.add("让小诉给你讲个故事吧");
                            moodResult.add(hollow.getStory());
                            break;
                    }
                }
                results.addAll(moodResult);
            }
        }
        if (results.size() == 0)
            results.add("小诉听不懂你说的什么诶，小苏笨死啦！");
        return results;
    }
}
