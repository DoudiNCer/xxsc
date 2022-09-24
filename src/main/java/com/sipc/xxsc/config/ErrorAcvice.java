package com.sipc.xxsc.config;

import club.javafamily.nf.request.FeiShuCardNotifyRequest;
import club.javafamily.nf.service.FeiShuNotifyHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipc.xxsc.pojo.dto.resultEnum.ResultEnum;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorAcvice {

    @Resource
    private FeiShuNotifyHandler feiShuNotifyHandler;

    /**
     * 全局捕获异常的切面类
     *
     * @param request  请求对象，可不传
     * @param response 响应对象，可不传
     * @param e        异常类(这个要和你当前捕获的异常类是同一个)
     */
    @ExceptionHandler(Exception.class) //也可以只对一个类进行捕获
    public void errorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {

        response.setCharacterEncoding("utf-8");
        /**
         * 处理已知异常
         */
        //不带token
        if ("没有token，重新登陆".equals(e.getMessage()) || "token验证失败，重新登陆".equals(e.getMessage())) {
            try {
                response.setStatus(200);
                Map<Object, Object> map = new HashMap<>();
                map.put("code", ResultEnum.TOKEN_ERROR.getCode());
                map.put("message", ResultEnum.TOKEN_ERROR.getMessage());
                map.put("operation", "请检查是否传了token或token是否在有效期");
                String jsonMap = new ObjectMapper().writeValueAsString(map);
                response.getWriter().write(jsonMap);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }
        //不带请求体
        if (e.getMessage().startsWith("Required request body is missing:")) {
            try {
                response.setStatus(200);
                Map<Object, Object> map = new HashMap<>();
                map.put("code", ResultEnum.TOKEN_ERROR.getCode());
                map.put("message", ResultEnum.TOKEN_ERROR.getMessage());
                map.put("operation", "非有效请求体，请检查");
                String jsonMap = new ObjectMapper().writeValueAsString(map);
                response.getWriter().write(jsonMap);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return;
        }

        /**
         * 处理未知异常
         */
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        sw.toString(); // stack trace as a string
        //获取当前时间
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");

        try {
            response.setStatus(500);
            Map<Object, Object> map = new HashMap<>();
            map.put("status", "Failed");
            map.put("message", "Internal Server Error");
            String jsonMap = new ObjectMapper().writeValueAsString(map);
            response.getWriter().write(jsonMap);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        e.printStackTrace();

        String dataTime = dateFormat.format(date);

        String content = "<at id=all></at>" +
                "\n报错时间: " + dataTime
                + "\npath: " + request.getRequestURI()
                + "\n异常: **" + e + "**"
                + "\n详细信息: \n" + sw;

        final FeiShuCardNotifyRequest tes
                = FeiShuCardNotifyRequest.of("DEV服务器内部错误", content,
                "可前往文档记录错误 :玫瑰:️ ✅ \uD83D\uDDA5️",
                "https://gagjcxhxrb.feishu.cn/sheets/shtcn0zAzzmzX17NqepNicers9R");

        feiShuNotifyHandler.notify(tes);
    }

}