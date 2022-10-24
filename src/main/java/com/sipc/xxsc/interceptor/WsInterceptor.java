package com.sipc.xxsc.interceptor;

import cn.hutool.http.HttpUtil;
import com.sipc.xxsc.mapper.AdvisoryMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipc.xxsc.pojo.domain.Advisory;
import com.sipc.xxsc.pojo.dto.CommonResult;
import com.sipc.xxsc.pojo.dto.param.advisory.WsConnectParam;
import com.sipc.xxsc.pojo.dto.resultEnum.ResultEnum;
import com.sipc.xxsc.util.CheckRole.CheckRole;
import com.sipc.xxsc.util.CheckRole.result.JWTCheckResult;
import com.sipc.xxsc.util.WebSocketUtils.AttributesKeys;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class WsInterceptor implements HandshakeInterceptor {
    @Resource
    private AdvisoryMapper advisoryMapper;
    /**
     * Invoked before the handshake is processed.
     *
     * @param request    the current request
     * @param response   the current response
     * @param wsHandler  the target WebSocket handler
     * @param attributes the attributes from the HTTP handshake to associate with the WebSocket
     *                   session; the provided attributes are copied, the original map is not used.
     * @return whether to proceed with the handshake ({@code true}) or abort ({@code false})
     */
    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, @NotNull Map<String, Object> attributes) {
        Map<String, String> params = HttpUtil.decodeParamMap(request.getURI().getQuery(), StandardCharsets.UTF_8);
        WsConnectParam param = new WsConnectParam();
        try {
            param.setToken(params.get("token"));
            param.setAdvisoryId(Integer.valueOf(params.get("advisoryId")));
        } catch (Exception e){
            log.warn(e.getMessage());
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                log.warn("\t" + stackTraceElement.toString());
            }
        }
        if (param.getToken() == null) {
            log.info("没有token");
        }

        CommonResult<JWTCheckResult> check = CheckRole.check(param.getToken());
        if (Objects.equals(check.getCode(), ResultEnum.SUCCESS.getCode())) {
            JWTCheckResult data = check.getData();
            if (param.getAdvisoryId() == 0){
                attributes.put(AttributesKeys.DOCISAI.getName(), Boolean.TRUE);
                attributes.put(AttributesKeys.ISDOCTOR.getName(), Boolean.FALSE);
                attributes.put(AttributesKeys.USER.getName(), data.getUserId());
                attributes.put(AttributesKeys.ADV.getName(), 0);
                attributes.put(AttributesKeys.DOCTOR.getName(), 0);
                return true;
            }
            Advisory advisory = advisoryMapper.selectById(param.getAdvisoryId());
            if (advisory == null)
                check = CommonResult.fail("预约不存在");
            else if (advisory.getDoctorId() == null)
                check = CommonResult.serverError();
            else if ((!Objects.equals(advisory.getDoctorId(), check.getData().getUserId()) && check.getData().getIsDoctor())
                    || (!Objects.equals(advisory.getUserId(), check.getData().getUserId()) && !check.getData().getIsDoctor()))
                check = CommonResult.userAuthError("预约与用户不符");
            else {
                if (data.getIsDoctor())
                    attributes.put(AttributesKeys.ISDOCTOR.getName(), Boolean.TRUE);
                else
                    attributes.put(AttributesKeys.ISDOCTOR.getName(), Boolean.FALSE);
                attributes.put(AttributesKeys.DOCISAI.getName(), Boolean.FALSE);
                attributes.put(AttributesKeys.USER.getName(), advisory.getUserId());
                attributes.put(AttributesKeys.ADV.getName(), advisory.getId());
                attributes.put(AttributesKeys.DOCTOR.getName(), advisory.getDoctorId());
                return true;
            }
        }
        try {
            //将 map装换为json ResponseBody底层使用jackson
            String json = new ObjectMapper().writeValueAsString(check);
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            response.getBody().flush();
            response.getBody().write(json.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.warn(e.getMessage());
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                log.warn("\t" + stackTraceElement.toString());
            }        }
        return false;
    }

    /**
     * Invoked after the handshake is done. The response status and headers indicate
     * the results of the handshake, i.e. whether it was successful or not.
     *
     * @param request   the current request
     * @param response  the current response
     * @param wsHandler the target WebSocket handler
     * @param exception an exception raised during the handshake, or {@code null} if none
     */
    @Override
    public void afterHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, Exception exception) {
        log.info("一个 WebSocket 连接结束，其请求为：");
        log.info("\t" + request);
        if (exception != null){
            log.info("抛出了异常：");
            log.warn(exception.getMessage());
            for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
                log.warn("\t" + stackTraceElement.toString());
            }
        }
    }
}
