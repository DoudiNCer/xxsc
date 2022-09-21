package com.sipc.xxsc.util.CheckRole;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sipc.xxsc.util.CheckRole.param.JWTPayloadParam;
import com.sipc.xxsc.util.CheckRole.result.JWTCheckResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Calendar;

public class JWTUtils {
    private static Logger logger = LoggerFactory.getLogger(JWTUtils.class);
    // 盐
    private static final String tokenPara = "p.iT|OnYm]:IRr1rW{E/~<5o_r+_ h@Eel/k kK?1n{|heX[Q4Tj_I#!*K8P=Y+Ru@";

    // 生成token
    public static String getToken(JWTPayloadParam payload){

        String payloadjson = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            StringWriter stringWriter = new StringWriter();
            JsonGenerator jsonGenerator = new JsonFactory().createGenerator(stringWriter);
            objectMapper.writeValue(jsonGenerator, payload);
            jsonGenerator.close();
            payloadjson = stringWriter.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH, 7);
        return JWT.create()
                .withClaim("userInfo", payloadjson)
                .withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC512(tokenPara));
    }
    // 验证token
    public static JWTCheckResult checkToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(tokenPara)).build();
        JWTCheckResult result = new JWTCheckResult();
        result.setRight(false);
        try {
            DecodedJWT verify = verifier.verify(token);
            String userInfo = verify.getClaim("userInfo").asString();
            ObjectMapper objectMapper = new ObjectMapper();
            JWTPayloadParam jwtPayloadParam = objectMapper.readValue(userInfo, JWTPayloadParam.class);
            result = new JWTCheckResult(jwtPayloadParam);
        } catch (JsonMappingException e) {
            logger.warn(e.toString());
        } catch (JWTVerificationException | JsonProcessingException e) {
            logger.warn(e.toString());
        }
        return result;
    }
}
