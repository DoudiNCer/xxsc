package com.sipc.xxsc.util.WebSocketUtils;

import com.sipc.xxsc.WSChatService.AdvisoryChatBWebSocket;
import com.sipc.xxsc.WSChatService.AdvisoryChatCWebSocket;
import com.sipc.xxsc.util.redis.RedisUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
@Component
public class UserDoctorUtil {
    @Resource
    private RedisUtil redisUtil;
    private static UserDoctorUtil userDoctorUtil;

    @PostConstruct
    public void init() {
        userDoctorUtil = this;
        userDoctorUtil.redisUtil = this.redisUtil;
    }
    private static final String DOCTORPRE = "DOCPRE";
    private static final String USERPRE = "USRPRE";
    public static final String  USERLST = "usrlst";
    public static final String DOCTORLST = "doclst";
    public static String getUserSessionCode(Integer userId){
        return USERPRE + userId + USERLST;
    }
    public static String getDoctorSessionCode(Integer doctorId){
        return DOCTORPRE + doctorId + DOCTORLST;
    }
    public static void storageUserSocket(Integer userId, AdvisoryChatCWebSocket socket){
        userDoctorUtil.redisUtil.set(getUserSessionCode(userId), socket);
    }
    public static void storageDoctorSocket(Integer doctorId, AdvisoryChatBWebSocket socket){
        userDoctorUtil.redisUtil.set(getDoctorSessionCode(doctorId), socket);
    }
    public static AdvisoryChatCWebSocket getUserSocket(Integer userId){
        String socketCode = getUserSessionCode(userId);
        if (!userDoctorUtil.redisUtil.exists(socketCode))
            return null;
        Object o = userDoctorUtil.redisUtil.get(socketCode);
        AdvisoryChatCWebSocket result = null;
        if (o instanceof AdvisoryChatCWebSocket)
            result = (AdvisoryChatCWebSocket) o;
        else
            userDoctorUtil.redisUtil.remove(socketCode);
        return result;
    }
    public static AdvisoryChatBWebSocket getDoctorSocket(Integer doctorId){
        String socketCode = getDoctorSessionCode(doctorId);
        if (!userDoctorUtil.redisUtil.exists(socketCode))
            return null;
        Object o = userDoctorUtil.redisUtil.get(socketCode);
        AdvisoryChatBWebSocket result = null;
        if (o instanceof AdvisoryChatBWebSocket)
            result = (AdvisoryChatBWebSocket) o;
        else
            userDoctorUtil.redisUtil.remove(socketCode);
        return result;
    }
    public static void userExit(Integer userId){
        userDoctorUtil.redisUtil.remove(getUserSessionCode(userId));
    }
    public static void doctorExit(Integer doctorId){
        userDoctorUtil.redisUtil.remove(getDoctorSessionCode(doctorId));
    }
}
