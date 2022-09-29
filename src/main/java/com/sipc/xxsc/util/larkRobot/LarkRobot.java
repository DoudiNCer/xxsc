package com.sipc.xxsc.util.larkRobot;

import com.sipc.xxsc.util.request.RequestUtils;
import com.sipc.xxsc.util.request.dto.Param;
import lombok.Data;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static com.sipc.xxsc.aop.WebLogAspect.getParameter;

@Data
@Component
@ConfigurationProperties(prefix = "lark-robot")
public class LarkRobot {

    private Boolean dev;

    private String url;

    private String profile;

    public void setDev(Boolean dev) {
        this.dev = dev;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String context(String title, String time, String errorUri, String errorType, String requestMessage) {
        String exception1 = "{\"config\": {\"wide_screen_mode\": true },\"elements\": [ {\"tag\": \"markdown\", \"content\": \"**";
        String exception2 = "**\\n";
        String exception3 = "\" }, { \"tag\": \"div\", \"fields\": [ {\"is_short\": true, \"text\": { \"tag\": \"lark_md\",\"content\": \"**\uD83D\uDDF3异常来源：**\\n";
        String exception4 = "\" } },{ \"is_short\": true,\"text\": {\"tag\": \"lark_md\", \"content\": \"**\uD83D\uDCDD异常类型：**\\n";
        String exception5 = "\"}}]},{\"tag\": \"markdown\", \"content\": \"请求详情：\\n";
        String exception6 = "\\n<at id=7013335291536719873></at>\"}]}";

        return exception1 + title + exception2 + time + exception3 + errorUri + exception4 + errorType + exception5 + requestMessage + exception6;
    }

    public synchronized void sendExceptionMessage(JoinPoint joinPoint, Exception exception) {

        if (exception.getClass().isAnnotationPresent(SkipNotice.class)) return;

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        String urlStr = request.getRequestURL().toString();

        @Data
        class Request {
            private String uri;
            private String method;
            private Object parameter;
        }

        Request webRequest = new Request();
        webRequest.setMethod(request.getMethod());
        webRequest.setParameter(getParameter(method, joinPoint.getArgs()));
        webRequest.setUri(request.getRequestURI());

        String title = "服务异常报告-"+profile;
        String message = context(title, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), request.getRequestURI(), exception.getClass().getName(), webRequest.toString());
        RequestUtils.POST(
                getUrl(),
                new Param(
                        "card", message,
                        "msg_type", "interactive"),
                Object.class
        );
        RequestUtils.POST(
                getUrl(),
                new Param(
                        "content", "{\"text\":\"" + exception.getMessage() + "\\n" + Arrays.toString(exception.getStackTrace()) + "\"}",
                        "msg_type", "text"
                ));
    }

    public void finish() {
        System.out.println();
        if (dev)
            return;
        RequestUtils.POST(
                getUrl(),
                new Param(
                        "content", "{\"text\":\"后端服务启动完成 --"+profile+"\"}",
                        "msg_type", "text"
                )
        );
    }

    @PreDestroy
    public void end() {
        if (dev)
            return;
        RequestUtils.POST(
                getUrl(),
                new Param(
                        "content", "{\"text\":\"后端服务开始关闭 --"+profile+"\"}",
                        "msg_type", "text"
                )
        );
    }

}
