package com.sipc.xxsc.util.WebSocketUtils;

import com.sipc.xxsc.mapper.AdvisoryMapper;
import com.sipc.xxsc.pojo.domain.Advisory;
import com.sipc.xxsc.pojo.dto.param.advisory.WsConnectParam;
import com.sipc.xxsc.util.WebSocketUtils.result.ParseAttributesResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class AdvisoryUtil {
    private static final String DOCTORP = "POCORP";
    private static final String USERP = "PUSERP";
    @Resource
    private AdvisoryMapper advisoryMapper;

    private static AdvisoryUtil advisoryUtil;

    public static ParseAttributesResult parseAttributes(Map<String, Object> attributes) {
        ParseAttributesResult result;
        try {
            result = new ParseAttributesResult();
            if (attributes.get(AttributesKeys.DOCISAI.getName()) != null){
                result.setDocIsAi(true);
                return result;
            }
            result.setDocIsAi(false);
            result.setIsDoctor((Boolean) attributes.get(AttributesKeys.ISDOCTOR.getName()));
            result.setAdvisoryId((Integer) attributes.get(AttributesKeys.ADV.getName()));
            result.setUserId((Integer) attributes.get(AttributesKeys.USER.getName()));
            result.setDoctorId((Integer) attributes.get(AttributesKeys.DOCTOR.getName()));
        } catch (Exception e){
            log.warn(e.getMessage());
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                log.warn(stackTraceElement.toString());
            }
            result = null;
        }
        return result;
    }

    @PostConstruct
    public void init() {
        advisoryUtil = this;
        advisoryUtil.advisoryMapper = this.advisoryMapper;
    }

    public static Advisory checkUserAndConsult(WsConnectParam param, Integer userId, Boolean isDoctor){
        Advisory advisory = advisoryUtil.advisoryMapper.selectById(param.getAdvisoryId());
        if (advisory == null)
            return advisory;
        if (!Objects.equals(userId, isDoctor ? advisory.getDoctorId() : advisory.getUserId()))
            advisory.setId(0);
        return advisory;
    }


    public static String getSessionCode(ParseAttributesResult param, Boolean isMe) {
        if (param.getIsDoctor() ^ isMe)
            return "B" + param.getDoctorId() + DOCTORP + param.getAdvisoryId();
        else
            return "C" + param.getAdvisoryId() + USERP + param.getUserId();
    }
}
