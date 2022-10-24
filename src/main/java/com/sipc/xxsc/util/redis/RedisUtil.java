package com.sipc.xxsc.util.redis;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisUtil {

    @Resource
    private RedisTemplate redisTemplate;

    public RedisUtil(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 写入String缓存
     * @param key 键
     * @param value 值
     * @return boolean
     */
    public boolean set(final String key,Object value){
        boolean result = false;
        try {
            ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            result = true;
        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }
    /**
     * 设置缓存过期时间
     * @param key 键
     * @param value 值
     * @param expireTime 过期时间
     * @param timeUnit timeUnit
     * @return boolean
     */
    public boolean set(final String key, Object value, Long expireTime, TimeUnit timeUnit){
        boolean result = false;
        try {
            ValueOperations<Serializable,Object> operations = redisTemplate.opsForValue();
            operations.set(key,value);
            redisTemplate.expire(key,expireTime,timeUnit);
            result = true;
        } catch(Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 判断缓存是否存在
     * @param key 键
     * @return boolean
     */
    public boolean exists(final String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 删除缓存
     * @param key 键
     */
    public void remove(final String key){
        if (exists(key)){
            redisTemplate.delete(key);
        }
    }

    /**
     * 取出缓存
     * @param key 键
     * @return 缓存
     */
    public Object get(final String key){
        Object result;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }



    public Long incr(final String key, Long delta) {
        if(delta<0){
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 向 set 添加参数
     * @param key 不能为空
     * @param values value
     */
    public Long sadd(Object key, List values){
        Long result = 0L;
        for (Object obj : values)
            result += redisTemplate.opsForSet().add(key, obj);
        return result;
    }
    public boolean setListByJson(String key, List value){
        String json = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            StringWriter stringWriter = new StringWriter();
            JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);
            objectMapper.writeValue(jsonGenerator, value);
            jsonGenerator.close();
            json = stringWriter.toString();
        } catch (Exception e){
            log.warn(e.getMessage());
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                log.warn("\t" + stackTraceElement.toString());
            }
        }
        if (json == null)
            return false;
        set(key, json);
        return true;
    }
    public <T> List<T> getListByJson(String key){
        Object o = get(key);
        if (o == null)
            return null;
        String json = null;
        try {
            json = (String) o;
        } catch (Exception e){
            log.warn(e.getMessage());
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                log.warn("\t" + stackTraceElement.toString());
            }
        }
        if (json == null)
            return null;
        List<T> result = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            result = objectMapper.readValue(json, List.class);
        } catch (Exception e){
            log.warn(e.getMessage());
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                log.warn("\t" + stackTraceElement.toString());
            }
        }
        return result;
    }

    /**
     * 随即从 set 中获取 n 个值
     */
    public List srandmember(Object key, long count){
        return redisTemplate.opsForSet().randomMembers(key, count);
    }
}
