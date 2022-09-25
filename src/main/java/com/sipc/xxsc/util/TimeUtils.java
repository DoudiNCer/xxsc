package com.sipc.xxsc.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
    /**
     * @apiNote 获取当前的10位时间戳（请确保服务器时间正确）
     * @return 十位时间戳
     */
    public static Long getNow(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis()/1000L;
    }

    /**
     * @apiNote 格式化时间戳未能时间
     * @param timeStamp 十位时间戳
     * @return "yyyy/MM/dd E" 的日期
     */
    public static String formatDateTime(Long timeStamp){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd E");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return simpleDateFormat.format(new Date(timeStamp * 1000));
    }
}
