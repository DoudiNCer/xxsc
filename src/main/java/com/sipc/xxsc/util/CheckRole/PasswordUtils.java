package com.sipc.xxsc.util.CheckRole;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordUtils {
    private static final String salt = "6szml.dzq/mao520Nv3LjkAJsSkd2vQV07jqSIJGtjLZgSasJ7X3AC";
    public static String getsPasswd(String passwd){
        String result = "ERR";
        try {
            MessageDigest message512Digest = MessageDigest.getInstance("SHA-512");
            message512Digest.update(passwd.getBytes());
            byte[] byteBuffer = message512Digest.digest();
            StringBuilder strHexString = new StringBuilder();
            for (byte b : byteBuffer) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    strHexString.append('0');
                }
                strHexString.append(hex);
            }
            String firstResult = strHexString.toString();
            String passwd1 = firstResult.concat(salt);
            MessageDigest message256Digest = MessageDigest.getInstance("SHA-256");
            message256Digest.update(passwd1.getBytes());
            byte[] byte2Buffer = message256Digest.digest();
            StringBuilder resultBuffer = new StringBuilder();
            for (byte b : byte2Buffer) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    resultBuffer.append('0');
                }
                resultBuffer.append(hex);
            }
            result = resultBuffer.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @apiNote 检查密码
     * @param passwd 用户输入的密码
     * @param passwdRes 数据库中的密码
     * @return 布尔值，表示两个密码是否相符
     */
    public static boolean checkPasswd(String passwd, String passwdRes){
        return passwdRes.equals(getsPasswd(passwd));
    }
}
